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

    public ResponseDTO<HousingDTO> requireHousing(String accessToken, Long housingId, BookingDTO booking) {

        UserEntity user = userRepo.findByAccessToken(accessToken);
        HousingEntity housing = housingRepo.getReferenceById(housingId);

        if (user != null) {

            try {
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
            } catch (EntityNotFoundException e) {
                return new ResponseDTO<>(null, "Failed to retrive housing by id", 404);
            }

            return new ResponseDTO<>(new HousingDTO(housing), "Housing requested", 200);
        } else
            return new ResponseDTO<>(null, "Failed to retrive user by access token", 401);
    }

    public ResponseDTO<List<BookingDTO>> getAllBookingRequestsByHost(String accessToken) {
        UserEntity host = userRepo.findByAccessToken(accessToken);

        if (host == null)
            return new ResponseDTO<>(null, "Failed to retrive user by access token", 401);

        List<BookingEntity> bookings = bookingRepo.findAllByHostName(host.getUsername());

        return new ResponseDTO<List<BookingDTO>>(bookings.stream().map(b -> new BookingDTO(b)).toList(), "", 200);
    }
}
