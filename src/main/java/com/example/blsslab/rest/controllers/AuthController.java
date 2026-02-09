package com.example.blsslab.rest.controllers;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody UserDTO user) {
        ResponseDTO<UUID> response = authService.signIn(user.getUsername(), user.getPassword());
        if (response.getEntity() != null) {
            ResponseCookie cookie = ResponseCookie.from("access-token", response.getEntity().toString()).path("/")
                    .maxAge(Duration.ofDays(1)).build();

            return ResponseEntity.status(response.getCode()).header("Set-Cookie", cookie.toString()).build();
        } else
            return new ResponseEntity<>(response.getMessage(), HttpStatusCode.valueOf(response.getCode()));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDTO user) {
        ResponseDTO<UUID> response = authService.signUp(user.getUsername(), user.getName(), user.getFamilyName(),
                user.getPassword());

        if (response.getEntity() != null) {
            ResponseCookie cookie = ResponseCookie.from("access-token", response.getEntity().toString()).path("/")
                    .maxAge(Duration.ofDays(1)).build();

            return ResponseEntity.status(response.getCode()).header("Set-Cookie", cookie.toString()).build();
        } else
            return new ResponseEntity<>(response.getMessage(), HttpStatusCode.valueOf(response.getCode()));
    }
}
