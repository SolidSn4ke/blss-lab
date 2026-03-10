package com.example.blsslab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.blsslab.exception.AlreadyProcessedException;
import com.example.blsslab.exception.BadRequestBodyException;
import com.example.blsslab.exception.RolePrivilegesViolationException;
import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.dto.RequestStatus;
import com.example.blsslab.model.dto.ResponseDTO;
import com.example.blsslab.model.dto.UserRole;
import com.example.blsslab.model.entity.AddressEntity;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.entity.UserEntity;
import com.example.blsslab.model.repos.AddressRepository;
import com.example.blsslab.model.repos.HousingRepository;
import com.example.blsslab.model.repos.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HousingService {

    HousingRepository housingRepo;

    UserRepository userRepo;

    AddressRepository addressRepo;

    public HousingService(HousingRepository housingRepo, UserRepository userRepo, AddressRepository addressRepo) {
        this.housingRepo = housingRepo;
        this.userRepo = userRepo;
        this.addressRepo = addressRepo;
    }

    public ResponseDTO<List<HousingDTO>> getAllHousings() {
        List<HousingEntity> housings = housingRepo.findAllByStatus(RequestStatus.CONFIRMED);
        return new ResponseDTO<List<HousingDTO>>(housings.stream().map(h -> new HousingDTO(h)).toList(), "", 200);
    }

    // public ResponseDTO<List<HousingDTO>> getAllHousingsToHandle(String username)
    // {
    // UserEntity user = userRepo.findById(username).orElse(null);

    // if (user == null) {
    // return new ResponseDTO<>(null, "Failed to retrieve user by username", 404);
    // }

    // if (user.getRole() != UserRole.MODERATOR) {
    // return new ResponseDTO<>(null, "Only moderator has access to this action",
    // 403);
    // }

    // List<HousingEntity> housings =
    // housingRepo.findAllByStatus(RequestStatus.PENDING);
    // return new ResponseDTO<List<HousingDTO>>(housings.stream().map(h -> new
    // HousingDTO(h)).toList(), "", 200);
    // }

    public ResponseDTO<HousingDTO> handleRequest(String username, Long id, Boolean approved) {
        UserEntity user = userRepo.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));

        if (user.getRole() != UserRole.MODERATOR) {
            throw new RolePrivilegesViolationException("Only moderator has access to this action");
        }

        if (approved == null) {
            throw new BadRequestBodyException("Field 'approved' is required");
        }

        HousingEntity housing = housingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve housing by id"));

        if (housing.getStatus() != RequestStatus.PENDING) {
            throw new AlreadyProcessedException("Housing request already processed");
        }

        if (approved) {
            housing.setStatus(RequestStatus.CONFIRMED);
        } else {
            housing.setStatus(RequestStatus.CANCELLED);
        }

        housingRepo.save(housing);

        return new ResponseDTO<>(new HousingDTO(housing), approved ? "Housing approved" : "Housing rejected", 200);
    }

    public ResponseDTO<HousingDTO> addHousing(HousingDTO housing) {
        UserEntity owner = userRepo.findById(housing.getOwner().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve user by username"));

        HousingEntity housingEntity = new HousingEntity();
        housingEntity.setPrice(housing.getPrice());
        housingEntity.setNumOfBeds(housing.getNumOfBeds());
        housingEntity.setRating(housing.getRating());
        housingEntity.setHousingType(housing.getHousingType());
        housingEntity.setStatus(RequestStatus.PENDING);
        housingEntity.setOwner(owner);

        AddressEntity address;

        if (housing.getAddress().getId() == null) {
            address = new AddressEntity();
            address.setStreet(housing.getAddress().getStreet());
            address.setCountry(housing.getAddress().getCountry());
            addressRepo.save(address);
        } else {
            address = addressRepo.findById(housing.getAddress().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Failed to retrieve address by id"));
        }

        housingEntity.setAddress(address);

        housingRepo.save(housingEntity);

        return new ResponseDTO<HousingDTO>(new HousingDTO(housingEntity),
                "Housing has been sent, await confirmation by moderator",
                200);
    }
}
