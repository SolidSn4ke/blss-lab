package com.example.blsslab.model.entity;

import java.util.Set;

import com.example.blsslab.model.dto.HousingType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
@Table(name = "housing")
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
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "booked_by")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "belongs_to")
    private UserEntity owner;

    @ManyToMany(mappedBy = "bookingRequests")
    private Set<UserEntity> requestedBy;
}
