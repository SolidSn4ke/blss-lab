package com.example.blsslab.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    HousingRepository housingRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    BookingRepository bookingRepo;

    public ResponseDTO<HousingDTO> requireHousing(String username, Long housingId, BookingDTO booking) {

        UserEntity user = userRepo.findById(username).orElse(null);
        HousingEntity housing = housingRepo.findById(housingId).orElse(null);

        if (user == null)
            return new ResponseDTO<>(null, "Failed to retrive user by username", 404);

        if (housing == null)
            return new ResponseDTO<>(null, "Failed to retrive housing by id", 404);

        LocalDate startDate = booking.getCheckIn();
        LocalDate endDate = booking.getCheckOut();
        if (LocalDate.now().isAfter(startDate)) {
            return new ResponseDTO<>(null, "Date of check in must be in the future", 400);
        }

        if (!startDate.isBefore(endDate)) {
            return new ResponseDTO<>(null, "Date of check in must be before date of check out", 400);
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days <= 0) {
            return new ResponseDTO<>(null, "Minimum period for booking must be at least 1 day", 400);
        }

        List<BookingEntity> existingBookings = bookingRepo.findAllByHousingIdAndStatus(
                housingId, RequestStatus.CONFIRMED);

        for (BookingEntity existingBooking : existingBookings) {
            LocalDate existingStart = existingBooking.getCheckIn();
            LocalDate existingEnd = existingBooking.getCheckOut();
            if (!(endDate.isBefore(existingStart) || startDate.isAfter(existingEnd))) {
                return new ResponseDTO<>(null,
                        "Unacceptable period of booking: there are conflicts with other bookings",
                        409);
            }
        }

        BookingEntity newBooking = new BookingEntity();
        newBooking.setCheckIn(booking.getCheckIn());
        newBooking.setCheckOut(booking.getCheckOut());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus(RequestStatus.PENDING);
        newBooking.setTotalPrice(housing.getPrice() * days);
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

    public ResponseDTO<List<BookingDTO>> getAllBookingRequestsByHost(String username) {
        UserEntity host = userRepo.getReferenceById(username);
        List<BookingEntity> bookings;

        try {
            bookings = bookingRepo.findAllByHostName(host.getUsername());
        } catch (EntityNotFoundException e) {
            return new ResponseDTO<>(null, "Failed to retrive user by username", 404);
        }

        return new ResponseDTO<List<BookingDTO>>(bookings.stream().map(b -> new BookingDTO(b)).toList(), "", 200);
    }

    public ResponseDTO<BookingDTO> handleRequest(String username, Long id, Boolean approved) {
        if (approved == null) {
            return new ResponseDTO<>(null, "Field 'approved' is required", 400);
        }

        BookingEntity booking = bookingRepo.findById(id).orElse(null);
        if (booking == null) {
            return new ResponseDTO<>(null, "Failed to retrieve booking by id", 404);
        }

        UserEntity owner = booking.getHousing().getOwner();

        if (owner == null || !owner.getUsername().equals(username)) {
            return new ResponseDTO<>(null, "Only owner can approve or deny request", 403);
        }

        if (booking.getStatus() != RequestStatus.PENDING) {
            return new ResponseDTO<>(null, "Booking request already processed", 409);
        }

        if (approved) {
            booking.setStatus(RequestStatus.CONFIRMED);
        } else {
            booking.setStatus(RequestStatus.CANCELLED);
        }

        bookingRepo.save(booking);

        return new ResponseDTO<>(new BookingDTO(booking), approved ? "Booking approved" : "Booking rejected", 200);
    }
}
