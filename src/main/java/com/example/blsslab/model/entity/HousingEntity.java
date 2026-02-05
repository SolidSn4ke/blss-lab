package com.example.blsslab.model.entity;

import com.example.blsslab.model.dto.HousingType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "appartment")
public class HousingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @NotNull
    private Long price;

    @Min(0)
    @NotNull
    private Integer numOfBeds;

    @Min(0)
    @Max(5)
    private Double rating;

    @Enumerated(EnumType.STRING)
    private HousingType type;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private UserEntity user;
}
