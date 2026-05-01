package com.example.blsslab.rest.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.blsslab.model.dto.ModerationRequest;
import com.example.blsslab.model.dto.PageInfo;
import com.example.blsslab.service.BookingService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    final BookingService bookingService;

    @PostMapping()
    public BookingDTO addBooking(@RequestBody BookingDTO booking) {
        BookingDTO response = bookingService.requireHousing(booking);
        return response;
    }

    @PatchMapping("/{id}")
    public BookingDTO updateBooking(@PathVariable Long id, @RequestBody BookingDTO entity) {
        BookingDTO response = bookingService.updateBooking(id, entity);
        return response;
    }

    @DeleteMapping("/{id}")
    public Boolean deleteBooking(@PathVariable Long id) {
        Boolean response = bookingService.deleteBooking(id);
        return response;
    }

    @GetMapping()
    public PageInfo<BookingDTO> getBookings(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "SENT") BookingType type,
            Pageable pageable) {
        return bookingService.getBookings(SecurityContextHolder.getContext().getAuthentication().getName(), type,
                search, pageable);
    }

    @PostMapping("/{id}/moderation")
    public BookingDTO handleRequest(
            @PathVariable Long id,
            @RequestBody ModerationRequest body) {
        BookingDTO response = bookingService.handleRequest(
                SecurityContextHolder.getContext().getAuthentication().getName(), id,
                body.getApproved());
        return response;
    }
}
