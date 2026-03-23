package com.example.blsslab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.blsslab.exception.AlreadyProcessedException;
import com.example.blsslab.exception.BadRequestBodyException;
import com.example.blsslab.model.dto.UserDTO;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepo;

    @Transactional
    public UserDTO addUser(UserDTO user) {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setName(user.getName());
        userEntity.setFamilyName(user.getFamilyName());
        userEntity.setRole(user.getRole());

        if (user.getUsername() == null) {
            throw new BadRequestBodyException("Username cannot be null");
        }

        UserEntity existUser = userRepo.findById(user.getUsername()).orElse(null);

        if (existUser != null) {
            throw new AlreadyProcessedException("User with this username is already exist");
        }

        userRepo.save(userEntity);

        return new UserDTO(userEntity);
    }

    @Transactional
    public UserDTO updateUser(String username, UserDTO userDTO) {
        UserEntity existUser = userRepo.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));

        existUser.update(userDTO);
        userRepo.save(existUser);
        return new UserDTO(existUser);
    }

    @Transactional
    public Boolean deleteUser(String username) {
        userRepo.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));
        userRepo.deleteById(username);
        return true;
    }
}
