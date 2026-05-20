package com.example.blsslab.model.mapper;

import org.springframework.stereotype.Component;

import com.example.blsslab.model.dto.AddressDTO;
import com.example.blsslab.model.postgres.entity.AddressEntity;

@Component
public class AddressMapper {
    public AddressDTO toDto(AddressEntity entity) {
        AddressDTO dto = new AddressDTO();
        dto.setId(entity.getId());
        dto.setStreet(entity.getStreet());
        dto.setCountry(entity.getCountry());
        return dto;
    }
}
