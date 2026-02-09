package com.example.blsslab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.HousingRepository;
import com.example.blsslab.model.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BookingService {

    @Autowired
    HousingRepository housingRepo;

    @Autowired
    UserRepository userRepo;

    public ResponseDTO<List<HousingDTO>> getAllHousings() {
        List<HousingEntity> housings = housingRepo.findAll();
        return new ResponseDTO<List<HousingDTO>>(housings.stream().map(h -> new HousingDTO(h)).toList(), "", 200);
    }

    public ResponseDTO<HousingDTO> requireHousing(String accessToken, Long housingId) {
        UserEntity user = userRepo.findByAccessToken(accessToken);
        HousingEntity housing = housingRepo.getReferenceById(housingId);

        if (user != null) {

            try {
                user.getBookingRequests().add(housing);
                housing.getRequestedBy().add(user);
                userRepo.save(user);
                housingRepo.save(housing);
            } catch (EntityNotFoundException e) {
                return new ResponseDTO<>(null, "Failed to retrive housing by id", 404);
            }

            return new ResponseDTO<>(new HousingDTO(housing), "Housing requested", 200);
        } else
            return new ResponseDTO<>(null, "Failed to retrive user by access token", 401);
    }
}
