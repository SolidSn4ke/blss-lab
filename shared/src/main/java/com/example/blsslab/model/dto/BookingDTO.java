package com.example.blsslab.model.dto;

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
    String guest;
    Long housingId;
    LocalDateTime createdAt;
    RequestStatus status;
    Long totalPrice;
    Integer adultsCount;
    Integer childCount;
    Integer infantsCount;
    Integer petCount;
}
