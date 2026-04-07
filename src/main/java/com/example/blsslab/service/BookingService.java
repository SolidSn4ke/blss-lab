package com.example.blsslab.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.exception.AlreadyProcessedException;
import com.example.blsslab.exception.BadRequestBodyException;
import com.example.blsslab.exception.RolePrivilegesViolationException;
import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.dto.BookingType;
import com.example.blsslab.model.dto.PageInfo;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.mysql.entity.BookingEntity;
import com.example.blsslab.model.mysql.repos.BookingRepository;
import com.example.blsslab.model.postgres.entity.HousingEntity;
import com.example.blsslab.model.postgres.repos.HousingRepository;
import com.example.blsslab.specs.CustomSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    final HousingRepository housingRepo;

    final BookingRepository bookingRepo;

    final XmlUserService xmlUserService;

    private void checkBookingPeriod(BookingDTO booking) {
        LocalDate startDate = booking.getCheckIn();
        LocalDate endDate = booking.getCheckOut();
        if (LocalDate.now().isAfter(startDate)) {
            throw new BadRequestBodyException("Date of check in must not be in the past");
        }

        if (!startDate.isBefore(endDate)) {
            throw new BadRequestBodyException("Date of check in must be before date of check out");
        }

        List<BookingEntity> existingBookings = bookingRepo.findAllByHousingIdAndStatus(
                booking.getHousingId(), RequestStatus.CONFIRMED);

        for (BookingEntity existingBooking : existingBookings) {
            LocalDate existingStart = existingBooking.getCheckIn();
            LocalDate existingEnd = existingBooking.getCheckOut();
            if (startDate.isBefore(existingEnd) && endDate.isAfter(existingStart)) {
                throw new BadRequestBodyException(
                        "Unacceptable period of booking: there are conflicts with other bookings");
            }
        }
    }

    @Transactional
    public BookingDTO requireHousing(BookingDTO booking) {

        UserDTO user = xmlUserService
                .getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new EntityNotFoundException("Failed to retrieve user by username");
        }
        HousingEntity housing = housingRepo.findById(booking.getHousingId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrive housing by id"));

        LocalDate startDate = booking.getCheckIn();
        LocalDate endDate = booking.getCheckOut();
        checkBookingPeriod(booking);

        BookingEntity newBooking = new BookingEntity();
        newBooking.setCheckIn(booking.getCheckIn());
        newBooking.setCheckOut(booking.getCheckOut());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus(RequestStatus.PENDING);
        newBooking.setTotalPrice(housing.getPrice() * ChronoUnit.DAYS.between(startDate, endDate));
        newBooking.setAdultsCount(booking.getAdultsCount());
        newBooking.setChildCount(booking.getChildCount());
        newBooking.setInfantsCount(booking.getInfantsCount());
        newBooking.setPetCount(booking.getPetCount());

        newBooking.setGuest(user.getUsername());
        newBooking.setHousingId(housing.getId());

        bookingRepo.save(newBooking);

        return new BookingDTO(newBooking);
    }

    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        BookingEntity existBooking = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve booking by id"));

        HousingEntity bookedHousing = housingRepo.findById(existBooking.getHousingId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve housing by id"));

        String callerUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!callerUsername.equals(existBooking.getGuest()) && !callerUsername.equals(bookedHousing.getOwner()) &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .contains(new SimpleGrantedAuthority("ROLE_MODER"))) {
            throw new RolePrivilegesViolationException(
                    "Only an owner, a guest or a moderator can change booking information");
        }

        existBooking.update(bookingDTO);
        existBooking.setCreatedAt(LocalDateTime.now());
        existBooking.setStatus(RequestStatus.PENDING);

        BookingDTO response = new BookingDTO(existBooking);
        checkBookingPeriod(response);
        bookingRepo.save(existBooking);
        return response;
    }

    @Transactional
    public Boolean deleteBooking(Long id) {
        BookingEntity booking = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve booking by id"));

        HousingEntity bookedHousing = housingRepo.findById(booking.getHousingId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve housing by id"));

        String callerUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!callerUsername.equals(booking.getGuest()) && !callerUsername.equals(bookedHousing.getOwner()) &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .contains(new SimpleGrantedAuthority("ROLE_MODER"))) {
            throw new RolePrivilegesViolationException(
                    "Only an owner, a guest or a moderator can change booking information");
        }

        bookingRepo.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public PageInfo<BookingDTO> getBookings(
            String username,
            BookingType type,
            String searchQuery,
            Pageable pageable) {
        StringBuilder sb = new StringBuilder(searchQuery == null ? "" : searchQuery);

        switch (type) {
            case SENT ->
                sb.append(String.format(";guest.username=%s", username));

            case RECIEVED -> sb.append(String.format(";housing.owner.username=%s", username));

            default -> sb.append("");
        }

        Page<BookingEntity> result = bookingRepo
                .findAllWithJoinFetch(CustomSpecification.buildFromFilters(sb.toString()),
                        pageable);

        List<BookingDTO> content = result.toList().stream().map(b -> new BookingDTO(b)).toList();
        return new PageInfo<BookingDTO>(content, result.getTotalPages(), result.getNumber(), result.getTotalElements());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BookingDTO handleRequest(String username, Long id, Boolean approved) {
        if (approved == null) {
            throw new BadRequestBodyException("Field 'approved' is required");
        }

        BookingEntity booking = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve booking by id"));

        UserDTO owner = xmlUserService.getUserByUsername(housingRepo.findById(booking.getHousingId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username")).getOwner());
        if (owner == null) {
            throw new EntityNotFoundException("Failed to retrieve user by username");
        }

        if (owner == null || !owner.getUsername().equals(username)) {
            throw new RolePrivilegesViolationException("Only owner can approve or deny request");
        }

        if (booking.getStatus() != RequestStatus.PENDING) {
            throw new AlreadyProcessedException("Booking request already processed");
        }

        if (approved)
            booking.setStatus(RequestStatus.CONFIRMED);
        else
            booking.setStatus(RequestStatus.CANCELLED);

        bookingRepo.save(booking);

        return new BookingDTO(booking);
    }
}
