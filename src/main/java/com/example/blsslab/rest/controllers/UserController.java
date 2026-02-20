package com.example.blsslab.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/add-user")
    public ResponseEntity<ResponseDTO<UserDTO>> addUser(@RequestBody UserDTO user) {
        ResponseDTO<UserDTO> response = userService.addUser(user);
        return new ResponseEntity<ResponseDTO<UserDTO>>(response, HttpStatusCode.valueOf(response.getCode()));
    }
}
