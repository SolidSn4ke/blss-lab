package com.example.blsslab.rest.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO<UserDTO>> addUser(@RequestBody UserDTO user) {
        ResponseDTO<UserDTO> response = userService.addUser(user);
        return new ResponseEntity<ResponseDTO<UserDTO>>(response, HttpStatusCode.valueOf(response.getCode()));
    }

    // TODO: Убрать void
    @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody UserDTO entity) {
        userService.updateUser(username, entity);
    }

    // TODO: Убрать void
    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
    }
}
