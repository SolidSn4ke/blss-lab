package com.example.blsslab.model.entity;

import java.util.Set;

import com.example.blsslab.model.dto.HousingType;
import com.example.blsslab.model.dto.RequestStatus;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "belongs_to")
    private UserEntity owner;

    @OneToMany(mappedBy = "housing")
    private Set<BookingEntity> bookingRequests;
}
