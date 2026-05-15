package com.example.blsslab.model.dto;

import com.example.blsslab.model.doctype.Convertable;
import com.example.blsslab.model.doctype.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HousingDTO implements Convertable<Item> {
    Long id;
    Long price;
    Double rating;
    Integer numOfBeds;
    HousingType housingType;
    AddressDTO address;
    String owner;

    @Override
    public Item toDocType() {
        Item item = new Item();
        item.setItem_code(this.id.toString());
        item.setItem_name(String.format("Housing %s in %s", housingType.name(), address.toString()));
        item.setItem_group("Services");
        item.setStock_uom("Square Meter");
        return item;
    }

}
