package com.example.blsslab.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.UserRepository;
import com.example.blsslab.util.Hex;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepo;

    MessageDigest digest;

    @PostConstruct
    private void init() {
        try {
            this.digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public ResponseDTO<UUID> signIn(String username, String password) {
        UserEntity user = userRepo.findById(username).orElse(null);
        if (user != null
                && user.getPassword()
                        .equals(Hex.bytesToHex(digest.digest(password.getBytes(StandardCharsets.UTF_8))))) {
            UUID accessToken = UUID.randomUUID();
            user.setAccessToken(accessToken.toString());
            userRepo.save(user);

            return new ResponseDTO<>(accessToken, "", 200);
        } else
            return new ResponseDTO<>(null, "Invalid username or password", 401);
    }

    public ResponseDTO<UUID> signUp(String username, String name, String familyName, String password) {
        if (userRepo.findById(username).isPresent())
            return new ResponseDTO<>(null, "This username is already taken", 401);

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setName(name);
        user.setFamilyName(familyName);
        user.setPassword(Hex.bytesToHex(digest.digest(password.getBytes(StandardCharsets.UTF_8))));

        UUID accessToken = UUID.randomUUID();
        user.setAccessToken(accessToken.toString());

        userRepo.save(user);
        return new ResponseDTO<>(accessToken, "", 200);
    }
}
