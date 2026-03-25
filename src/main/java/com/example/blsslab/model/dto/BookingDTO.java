package com.example.blsslab.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.blsslab.model.db2.entity.BookingEntity;

@Getter
@Setter
@NoArgsConstructor
public class BookingDTO {
    Long id;
    LocalDate checkIn;
    LocalDate checkOut;
    String guest;
    Long housingId;
    LocalDateTime createdAt;
    RequestStatus status;
    Long totalPrice;
    Integer adultsCount;
    Integer childCount;
    Integer infantsCount;
    Integer petCount;

    public BookingDTO(BookingEntity bookingEntity) {
        this.id = bookingEntity.getId();
        this.checkIn = bookingEntity.getCheckIn();
        this.checkOut = bookingEntity.getCheckOut();
        this.guest = bookingEntity.getGuest();
        this.housingId = bookingEntity.getHousing();
        this.createdAt = bookingEntity.getCreatedAt();
        this.status = bookingEntity.getStatus();
        this.totalPrice = bookingEntity.getTotalPrice();
        this.adultsCount = bookingEntity.getAdultsCount();
        this.childCount = bookingEntity.getChildCount();
        this.infantsCount = bookingEntity.getInfantsCount();
        this.petCount = bookingEntity.getPetCount();
    }
}
