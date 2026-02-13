package com.example.blsslab.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.entity.AddressEntity;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.entity.LocationEntity;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.AddressRepository;
import com.example.blsslab.model.repos.HousingRepository;
import com.example.blsslab.model.repos.LocationRepository;
import com.example.blsslab.model.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HousingService {

    @Autowired
    HousingRepository housingRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    AddressRepository addressRepo;

    @Autowired
    LocationRepository locationRepo;

    public ResponseDTO<List<HousingDTO>> getAllHousings() {
        List<HousingEntity> housings = housingRepo.findAllByStatus(RequestStatus.CONFIRMED);
        return new ResponseDTO<List<HousingDTO>>(housings.stream().map(h -> new HousingDTO(h)).toList(), "", 200);
    }

    public ResponseDTO<HousingDTO> addHousing(String username, HousingDTO housing) {
        UserEntity owner = userRepo.findById(username).orElse(null);

        if (owner == null)
            return new ResponseDTO<>(null, "Failed to retrieve user by username", 404);

        HousingEntity housingEntity = new HousingEntity();
        housingEntity.setPrice(housing.getPrice());
        housingEntity.setNumOfBeds(housing.getNumOfBeds());
        housingEntity.setRating(housing.getRating());
        housingEntity.setType(housing.getType());
        housingEntity.setStatus(RequestStatus.PENDING);
        housingEntity.setOwner(owner);

        AddressEntity address;
        LocationEntity location;

        if (housing.getAddress().getId() == null) {
            address = new AddressEntity();
            address.setStreet(housing.getAddress().getStreet());
            location = locationRepo.getReferenceById(housing.getAddress().getLocation().getId());

            try {
                address.setLocation(location);
            } catch (EntityNotFoundException e) {
                return new ResponseDTO<HousingDTO>(null, "Failed to retrieve location by id", 404);
            }
            addressRepo.save(address);

        } else {
            address = addressRepo.findById(housing.getAddress().getId()).orElse(null);

            if (address == null)
                return new ResponseDTO<>(null, "Failed to retrieve address by id", 404);
        }

        housingEntity.setAddress(address);

        housingRepo.save(housingEntity);

        return new ResponseDTO<HousingDTO>(new HousingDTO(housingEntity),
                "Housing has been sent, await confirmation by moderator",
                200);
    }
}
