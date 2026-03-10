package com.example.blsslab.rest.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.dto.BookingType;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ModerationRequest;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.service.BookingService;
import org.springframework.web.bind.annotation.PutMapping;

// TODO: Редактирование, удаление
@RestController
@RequestMapping("/bookings")
public class BookingController {

    BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public ResponseDTO<HousingDTO> addBooking(@RequestBody BookingDTO booking) {
        ResponseDTO<HousingDTO> response = bookingService.requireHousing(booking);
        return response;
    }

    // TODO: Убрать void
    @PutMapping("/{id}")
    public void updateBooking(@PathVariable Long id, @RequestBody BookingDTO entity) {
        bookingService.updateBooking(id, entity);
    }

    // TODO: Убрать void
    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    // Пейджинг, фильтрация, сортировка
    @GetMapping()
    public ResponseDTO<List<BookingDTO>> getBookings(@RequestParam String username,
            @RequestParam(defaultValue = "ALL") BookingType type) {
        ResponseDTO<List<BookingDTO>> response;
        switch (type) {
            case SENT -> response = bookingService.getAllBookingRequestsByUser(username);
            case RECIEVED -> response = bookingService.getAllBookingRequestsByHost(username);
            default -> response = null; // TODO: Добавить getAllBookings принадлежащие одному пользователю
        }
        return response;
    }

    @PostMapping("/{id}/moderation")
    public ResponseDTO<BookingDTO> handleRequest(
            @PathVariable Long id,
            @RequestBody ModerationRequest body) {
        ResponseDTO<BookingDTO> response = bookingService.handleRequest(body.getUser().getUsername(), id,
                body.getApproved());
        return response;
    }
}
