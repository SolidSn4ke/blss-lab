package com.example.blsslab.rest.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.service.XmlUserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    final XmlUserService xmlUserService;

    @GetMapping()
    public UserDTO getInfo() {
        return xmlUserService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PatchMapping()
    public boolean update(@RequestBody UserDTO newInfo) {
        return xmlUserService.updateUser(SecurityContextHolder.getContext().getAuthentication().getName(), newInfo);
    }

    @DeleteMapping()
    public boolean delete() {
        return xmlUserService.deleteUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
