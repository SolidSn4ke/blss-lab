package com.example.blsslab.model.dto;

import com.example.blsslab.model.entity.HousingEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HousingDTO {
    Long id;
    Long price;
    Integer numOfBeds;
    HousingType type;

    public HousingDTO(HousingEntity housingEntity) {
        this.id = housingEntity.getId();
        this.price = housingEntity.getPrice();
        this.numOfBeds = housingEntity.getNumOfBeds();
        this.type = housingEntity.getType();
    }
}
