package com.example.blsslab.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.config.MqttConfig.MqttGateway;
import com.example.blsslab.exception.AlreadyProcessedException;
import com.example.blsslab.exception.BadRequestBodyException;
import com.example.blsslab.exception.RolePrivilegesViolationException;
import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.dto.BookingType;
import com.example.blsslab.model.dto.PageInfo;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.mapper.BookingMapper;
import com.example.blsslab.model.mysql.entity.BookingEntity;
import com.example.blsslab.model.mysql.repos.BookingRepository;
import com.example.blsslab.model.postgres.entity.HousingEntity;
import com.example.blsslab.model.postgres.repos.HousingRepository;
import com.example.blsslab.specs.CustomSpecification;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    final HousingRepository housingRepo;

    final BookingRepository bookingRepo;

    final XmlUserService xmlUserService;

    final MqttGateway gateway;

    final BookingMapper bookingMapper;

    private void checkBookingPeriod(BookingDTO bookingDTO) {
        log.debug(
                "Validating booking period: housingId={}, checkIn={}, checkOut={}",
                bookingDTO.getHousingId(),
                bookingDTO.getCheckIn(),
                bookingDTO.getCheckOut());

        LocalDate startDate = bookingDTO.getCheckIn();
        LocalDate endDate = bookingDTO.getCheckOut();
        if (LocalDate.now().isAfter(startDate)) {
            throw new BadRequestBodyException("Date of check in must not be in the past");
        }

        if (!startDate.isBefore(endDate)) {
            throw new BadRequestBodyException("Date of check in must be before date of check out");
        }

        List<BookingEntity> existingBookings = bookingRepo.findAllByHousingIdAndStatus(
                bookingDTO.getHousingId(), RequestStatus.CONFIRMED);

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
    public BookingDTO requireHousing(BookingDTO bookingDTO) {

        UserDTO user = xmlUserService
                .getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) {
            throw new EntityNotFoundException("Failed to retrieve user by username");
        }

        log.info("Booking creation requested: user={}, housingId={}, checkIn={}, checkOut={}",
                user.getUsername(),
                bookingDTO.getHousingId(),
                bookingDTO.getCheckIn(),
                bookingDTO.getCheckOut());

        HousingEntity housing = housingRepo.findById(bookingDTO.getHousingId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrive housing by id"));

        LocalDate startDate = bookingDTO.getCheckIn();
        LocalDate endDate = bookingDTO.getCheckOut();
        checkBookingPeriod(bookingDTO);

        BookingEntity newBooking = new BookingEntity();
        newBooking.setCheckIn(bookingDTO.getCheckIn());
        newBooking.setCheckOut(bookingDTO.getCheckOut());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus(RequestStatus.PENDING);
        newBooking.setTotalPrice(housing.getPrice() * ChronoUnit.DAYS.between(startDate, endDate));
        newBooking.setAdultsCount(bookingDTO.getAdultsCount());
        newBooking.setChildCount(bookingDTO.getChildCount());
        newBooking.setInfantsCount(bookingDTO.getInfantsCount());
        newBooking.setPetCount(bookingDTO.getPetCount());

        newBooking.setGuest(user.getUsername());
        newBooking.setHousingId(housing.getId());

        bookingRepo.save(newBooking);

        log.info(
                "Booking created successfully: bookingId={}, guest={}, housingId={}, status={}",
                newBooking.getId(),
                newBooking.getGuest(),
                newBooking.getHousingId(),
                newBooking.getStatus());

        return bookingMapper.toDto(newBooking);
    }

    @Transactional
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        log.info("Booking update requested: bookingId={}", id);

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

        BookingDTO response = bookingMapper.toDto(existBooking);
        checkBookingPeriod(response);
        bookingRepo.save(existBooking);

        log.info("Booking updated successfully: bookingId={}, newStatus={}",
                existBooking.getId(),
                existBooking.getStatus());
        return response;
    }

    @Transactional
    public Boolean deleteBooking(Long id) {
        log.info("Booking delete requested: bookingId={}", id);

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

        log.info("Booking deleted successfully: bookingId={}", id);
        return true;
    }

    @Transactional(readOnly = true)
    public PageInfo<BookingDTO> getBookings(
            String username,
            BookingType type,
            String searchQuery,
            Pageable pageable) {
        StringBuilder sb = new StringBuilder(searchQuery == null ? "" : searchQuery);
        final List<Long> ownedHousings = new ArrayList<>();

        switch (type) {
            case SENT ->
                sb.append(String.format(";guest=%s", username));

            case RECIEVED -> {
                housingRepo
                        .findAll(CustomSpecification.buildFromFilters(String.format("owner=%s", username)), pageable)
                        .forEach(e -> ownedHousings.add(e.getId()));
            }

            default -> sb.append("");
        }

        Page<BookingEntity> result = bookingRepo.findAll(CustomSpecification.buildFromFilters(sb.toString()), pageable);
        Stream<BookingEntity> stream = result.stream();

        if (type == BookingType.RECIEVED) {
            stream = stream.filter(e -> ownedHousings.contains(e.getHousingId()));
        }

        List<BookingDTO> content = stream.map(b -> bookingMapper.toDto(b)).toList();
        return new PageInfo<BookingDTO>(content, result.getTotalPages(), result.getNumber(), result.getTotalElements());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BookingDTO handleRequest(String username, Long id, Boolean approved) {
        log.info("Booking handle requested: bookingId={}, user={}, approved={}", id, username, approved);

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

        log.info("Booking handled successfully: bookingId={}, newStatus={}", id, booking.getStatus());
        return bookingMapper.toDto(booking);
    }
}
