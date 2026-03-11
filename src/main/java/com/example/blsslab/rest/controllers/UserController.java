package com.example.blsslab.rest.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public UserDTO addUser(@RequestBody UserDTO user) {
        UserDTO response = userService.addUser(user);
        return response;
    }

    @PatchMapping("/{username}")
    public UserDTO updateUser(@PathVariable String username, @RequestBody UserDTO entity) {
        UserDTO response = userService.updateUser(username, entity);
        return response;
    }

    @DeleteMapping("/{username}")
    public Boolean deleteUser(@PathVariable String username) {
        Boolean response = userService.deleteUser(username);
        return response;
    }
}
