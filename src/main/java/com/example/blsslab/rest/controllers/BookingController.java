package com.example.blsslab.rest.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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

    @GetMapping("/housings")
    public ResponseEntity<List<HousingDTO>> getHousings() {
        ResponseDTO<List<HousingDTO>> response = bookingService.getAllHousings();
        return new ResponseEntity<List<HousingDTO>>(response.getEntity(), HttpStatusCode.valueOf(response.getCode()));
    }

    @PostMapping("/require-housing/{id}")
    public ResponseEntity<ResponseDTO<HousingDTO>> requireHousing(@PathVariable Long id,
            @CookieValue(name = "access-token") String accessToken, @RequestBody BookingDTO booking) {
        ResponseDTO<HousingDTO> response = bookingService.requireHousing(accessToken, id, booking);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    @GetMapping("/recieved-requests")
    public ResponseEntity<ResponseDTO<List<BookingDTO>>> getRecievedRequests(
            @CookieValue(name = "access-token") String accessToken) {
        ResponseDTO<List<BookingDTO>> response = bookingService.getAllBookingRequestsByHost(accessToken);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    @PostMapping("/handle-request/{id}")
    public ResponseEntity<ResponseDTO<BookingDTO>> handleRequest(
            @PathVariable Long id,
            @CookieValue(name = "access-token") String accessToken,
            @RequestBody Map<String, Boolean> body) {
        Boolean approved = body.get("approved");
        ResponseDTO<BookingDTO> response = bookingService.handleRequest(accessToken, id, approved);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
    }
}
