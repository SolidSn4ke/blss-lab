package com.example.blsslab.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    public ResponseDTO<UserDTO> addUser(UserDTO user) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(user.getPassword());
        userEntity.setName(user.getName());
        userEntity.setFamilyName(user.getFamilyName());
        userEntity.setRole(user.getRole());

        UserEntity existUser = userRepo.findById(user.getUsername()).orElse(null);
        if (existUser != null) {
            return new ResponseDTO<>(null, "User with this username is already exist", 409);
        }

        userRepo.save(userEntity);

        return new ResponseDTO<UserDTO>(new UserDTO(userEntity), "User has been added", 200);
    }
}
