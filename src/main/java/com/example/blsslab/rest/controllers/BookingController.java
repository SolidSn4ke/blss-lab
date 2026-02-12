package com.example.blsslab.rest.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @PostMapping("/{username}/require-housing/{id}")
    public ResponseEntity<ResponseDTO<HousingDTO>> requireHousing(@PathVariable Long id,
            @PathVariable String username,
            @RequestBody BookingDTO booking) {
        ResponseDTO<HousingDTO> response = bookingService.requireHousing(username, id, booking);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    @GetMapping("/{username}/recieved-requests")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> getRecievedRequests(@PathVariable String username) {
        ResponseDTO<List<BookingDTO>> response = bookingService.getAllBookingRequestsByHost(username);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    @PostMapping("/{username}/handle-request/{id}")
    public ResponseEntity<ResponseDTO<BookingDTO>> handleRequest(
            @PathVariable Long id,
            @PathVariable String username,
            @RequestBody Map<String, Boolean> body) {
        Boolean approved = body.get("approved");
        ResponseDTO<BookingDTO> response = bookingService.handleRequest(username, id, approved);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

}
