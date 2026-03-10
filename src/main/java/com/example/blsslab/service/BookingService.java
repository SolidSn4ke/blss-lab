package com.example.blsslab.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.blsslab.exception.AlreadyProcessedException;
import com.example.blsslab.exception.BadRequestBodyException;
import com.example.blsslab.exception.RolePrivilegesViolationException;
import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.entity.BookingEntity;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.BookingRepository;
import com.example.blsslab.model.repos.HousingRepository;
import com.example.blsslab.model.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookingService {

    HousingRepository housingRepo;

    UserRepository userRepo;

    BookingRepository bookingRepo;

    public BookingService(HousingRepository housingRepo, UserRepository userRepo, BookingRepository bookingRepo) {
        this.housingRepo = housingRepo;
        this.userRepo = userRepo;
        this.bookingRepo = bookingRepo;
    }

    public ResponseDTO<HousingDTO> requireHousing(BookingDTO booking) {

        UserEntity user = userRepo.findById(booking.getGuest().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrive user by username"));
        HousingEntity housing = housingRepo.findById(booking.getHousing().getId())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrive housing by id"));

        LocalDate startDate = booking.getCheckIn();
        LocalDate endDate = booking.getCheckOut();
        if (LocalDate.now().isAfter(startDate)) {
            throw new BadRequestBodyException("Date of check in must not be in the past");
        }

        if (!startDate.isBefore(endDate)) {
            throw new BadRequestBodyException("Date of check in must be before date of check out");
        }

        List<BookingEntity> existingBookings = bookingRepo.findAllByHousingIdAndStatus(
                booking.getHousing().getId(), RequestStatus.CONFIRMED);

        for (BookingEntity existingBooking : existingBookings) {
            LocalDate existingStart = existingBooking.getCheckIn();
            LocalDate existingEnd = existingBooking.getCheckOut();
            if (startDate.isBefore(existingEnd) && endDate.isAfter(existingStart)) {
                throw new BadRequestBodyException(
                        "Unacceptable period of booking: there are conflicts with other bookings");
            }
        }

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

        newBooking.setGuest(user);
        newBooking.setHousing(housing);

        bookingRepo.save(newBooking);
        userRepo.save(user);
        housingRepo.save(housing);

        return new ResponseDTO<>(new HousingDTO(housing), "Housing requested", 200);
    }

    // TODO: Убрать void
    public void updateBooking(Long id, BookingDTO booking) {
        BookingEntity bookingEntity = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve booking by id"));
        bookingEntity.setCheckIn(booking.getCheckIn());
        bookingEntity.setCheckOut(booking.getCheckOut());
        bookingEntity.setCreatedAt(LocalDateTime.now());
        bookingEntity.setStatus(RequestStatus.PENDING);
        bookingEntity.setTotalPrice(booking.getTotalPrice());
        bookingEntity.setAdultsCount(booking.getAdultsCount());
        bookingEntity.setChildCount(booking.getChildCount());
        bookingEntity.setInfantsCount(booking.getInfantsCount());
        bookingEntity.setPetCount(booking.getPetCount());

        bookingRepo.save(bookingEntity);
    }

    // TODO: Убрать void
    public void deleteBooking(Long id) {
        bookingRepo.deleteById(id);
    }

    public ResponseDTO<List<BookingDTO>> getAllBookingRequestsByHost(String username) {
        UserEntity host = userRepo.getReferenceById(username);
        List<BookingEntity> bookings;

        bookings = bookingRepo.findAllByHostName(host.getUsername());

        return new ResponseDTO<List<BookingDTO>>(bookings.stream().map(b -> new BookingDTO(b)).toList(), "", 200);
    }

    public ResponseDTO<List<BookingDTO>> getAllBookingRequestsByUser(String username) {
        UserEntity user = userRepo.getReferenceById(username);
        List<BookingEntity> bookings;

        bookings = bookingRepo.findAllByUserName(user.getUsername());

        return new ResponseDTO<List<BookingDTO>>(bookings.stream().map(b -> new BookingDTO(b)).toList(), "", 200);
    }

    public ResponseDTO<BookingDTO> handleRequest(String username, Long id, Boolean approved) {
        if (approved == null) {
            throw new BadRequestBodyException("Field 'approved' is required");
        }

        BookingEntity booking = bookingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve booking by id"));

        UserEntity owner = booking.getHousing().getOwner();

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

        return new ResponseDTO<>(new BookingDTO(booking), approved ? "Booking approved" : "Booking rejected", 200);
    }
}
