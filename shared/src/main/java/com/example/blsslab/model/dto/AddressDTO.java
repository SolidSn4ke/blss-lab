package com.example.blsslab.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    Long id;
    String street;
    Country country;

    @Override
    public String toString() {
        return String.format("%s, %s", street, country.name());
    }
}
