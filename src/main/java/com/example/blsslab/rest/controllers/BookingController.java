package com.example.blsslab.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @GetMapping("/housings")
    public ResponseEntity getAppartments() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/require-housing/{id}")
    public ResponseEntity requireHousing(){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
