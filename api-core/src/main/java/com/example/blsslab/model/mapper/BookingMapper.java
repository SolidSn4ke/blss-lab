package com.example.blsslab.model.mapper;

import org.springframework.stereotype.Component;

import com.example.blsslab.model.dto.BookingDTO;
import com.example.blsslab.model.mysql.entity.BookingEntity;

@Component
public class BookingMapper {
    public BookingDTO toDto(BookingEntity entity) {
        BookingDTO dto = new BookingDTO();
        dto.setId(entity.getId());
        dto.setCheckIn(entity.getCheckIn());
        dto.setCheckOut(entity.getCheckOut());
        dto.setGuest(entity.getGuest());
        dto.setHousingId(entity.getHousingId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setAdultsCount(entity.getAdultsCount());
        dto.setChildCount(entity.getChildCount());
        dto.setInfantsCount(entity.getInfantsCount());
        dto.setPetCount(entity.getPetCount());
        return dto;
    }
}
