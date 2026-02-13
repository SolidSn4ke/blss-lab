package com.example.blsslab.model.dto;

import com.example.blsslab.model.entity.LocationEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {
    Long id;
    Country country;

    public LocationDTO(LocationEntity locationEntity) {
        this.id = locationEntity.getId();
        this.country = locationEntity.getCountry();
    }
}