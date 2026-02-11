package com.example.blsslab.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.dto.BookingStatus;
import com.example.blsslab.model.dto.HousingDTO;
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

    public ResponseDTO<List<HousingDTO>> getAllHousings() {
        List<HousingEntity> housings = housingRepo.findAll();
        return new ResponseDTO<List<HousingDTO>>(housings.stream().map(h -> new HousingDTO(h)).toList(), "", 200);
    }

    // TODO: добавить проверку корректности дат, расчитывать стоимость правильно
    public ResponseDTO<HousingDTO> requireHousing(String username, Long housingId, BookingDTO booking) {

        UserEntity user = userRepo.findById(username).orElse(null);
        HousingEntity housing = housingRepo.findById(housingId).orElse(null);

        if (user == null)
            return new ResponseDTO<>(null, "Failed to retrive user by username", 404);

        if (housing == null)
            return new ResponseDTO<>(null, "Failed to retrive housing by id", 404);

        BookingEntity newBooking = new BookingEntity();
        newBooking.setCheckIn(booking.getCheckIn());
        newBooking.setCheckOut(booking.getCheckOut());
        newBooking.setCreatedAt(LocalDateTime.now());
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setTotalPrice(housing.getPrice());
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

        if (booking.getStatus() != BookingStatus.PENDING) {
            return new ResponseDTO<>(null, "Booking request already processed", 409);
        }

        if (approved) {
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            booking.setStatus(BookingStatus.CANCELLED);
        }

        bookingRepo.save(booking);

        return new ResponseDTO<>(new BookingDTO(booking), approved ? "Booking approved" : "Booking rejected", 200);
    }
}
