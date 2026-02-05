package com.example.blsslab.rest.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/sign-in")
    public ResponseEntity signIn() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
