package com.example.blsslab.service;

import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.UserRepository;

@Service
public class UserService {

    UserRepository userRepo;

    public UserService(UserRepository userRepository){
        this.userRepo = userRepository;
    }

    public ResponseDTO<UserDTO> addUser(UserDTO user) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setName(user.getName());
        userEntity.setFamilyName(user.getFamilyName());
        userEntity.setRole(user.getRole());

        if (user.getUsername() == null) {
            return new ResponseDTO<>(null, "Username cannot be null", 400);
        }

        UserEntity existUser = userRepo.findById(user.getUsername()).orElse(null);

        if (existUser != null) {
            return new ResponseDTO<>(null, "User with this username is already exist", 409);
        }

        userRepo.save(userEntity);

        return new ResponseDTO<UserDTO>(new UserDTO(userEntity), "User has been added", 200);
    }
}
