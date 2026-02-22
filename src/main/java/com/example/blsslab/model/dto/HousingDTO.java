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
    Double rating;
    Integer numOfBeds;
    HousingType housingType;
    AddressDTO address;

    public HousingDTO(HousingEntity housingEntity) {
        this.id = housingEntity.getId();
        this.price = housingEntity.getPrice();
        this.rating = housingEntity.getRating();
        this.numOfBeds = housingEntity.getNumOfBeds();
        this.housingType = housingEntity.getHousingType();
        this.address = new AddressDTO(housingEntity.getAddress());
    }
}
