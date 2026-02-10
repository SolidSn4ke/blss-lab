package com.example.blsslab.model.entity;

import com.example.blsslab.model.dto.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private UserEntity guest;

    @ManyToOne
    @JoinColumn(name = "housing_id")
    private HousingEntity housing;

    @NotNull
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Long totalPrice;

    @Min(1)
    private Integer adultsCount;

    @Min(0)
    private Integer childCount;

    @Min(0)
    private Integer infantsCount;

    @Min(0)
    private Integer petCount;
}
