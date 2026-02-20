package com.example.blsslab.model.dto;

import com.example.blsslab.model.entity.AddressEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {
    Long id;
    String street;
    Country country;

    public AddressDTO(AddressEntity addressEntity) {
        this.id = addressEntity.getId();
        this.street = addressEntity.getStreet();
        this.country = addressEntity.getCountry();
    }

}
