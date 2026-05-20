package com.example.blsslab.model.mapper;

import org.springframework.stereotype.Component;

import com.example.blsslab.model.dto.HousingDTO;
import com.example.blsslab.model.postgres.entity.HousingEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HousingMapper {
    final AddressMapper addressMapper;

    public HousingDTO toDto(HousingEntity housingEntity) {
        HousingDTO dto = new HousingDTO();

        dto.setId(housingEntity.getId());
        dto.setPrice(housingEntity.getPrice());
        dto.setRating(housingEntity.getRating());
        dto.setNumOfBeds(housingEntity.getNumOfBeds());
        dto.setHousingType(housingEntity.getHousingType());
        dto.setAddress(addressMapper.toDto(housingEntity.getAddress()));
        dto.setOwner(housingEntity.getOwner());
        dto.setStatus(housingEntity.getStatus());

        return dto;
    }
}
