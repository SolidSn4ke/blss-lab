package com.example.blsslab.model.dto;

import com.example.blsslab.model.entity.BookingEntity;
import com.example.blsslab.model.entity.HousingEntity;
import com.example.blsslab.model.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookingDTO {
    Long id;
    LocalDate checkIn;
    LocalDate checkOut;
    UserEntity guest;
    HousingEntity housing;
    LocalDateTime createdAt;
    BookingStatus status;
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
        this.housing = bookingEntity.getHousing();
        this.createdAt = bookingEntity.getCreatedAt();
        this.status = bookingEntity.getStatus();
        this.totalPrice = bookingEntity.getTotalPrice();
        this.adultsCount = bookingEntity.getAdultsCount();
        this.childCount = bookingEntity.getChildCount();
        this.infantsCount = bookingEntity.getInfantsCount();
        this.petCount = bookingEntity.getPetCount();
    }
}
