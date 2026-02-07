package com.example.blsslab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.repos.HousingRepository;

@Service
public class BookingService {

    @Autowired
    HousingRepository housingRepo;

    public ResponseDTO<List<HousingDTO>> getAllHousings() {
        List<HousingEntity> housings = housingRepo.findAll();
        return new ResponseDTO<List<HousingDTO>>(housings.stream().map(h -> new HousingDTO(h)).toList(), "", 200);
    }
}
