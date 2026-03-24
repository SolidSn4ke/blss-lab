package com.example.blsslab.rest.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.jaas.JaasAuthenticationProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.service.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    final JaasAuthenticationProvider provider;

    final JwtService jwtService;

    @PostMapping("/login")
    public String postMethodName(@RequestBody UserDTO userCredentials) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                userCredentials.getUsername(), userCredentials.getPassword());
        provider.authenticate(credentials);
        return "true";
    }
}
