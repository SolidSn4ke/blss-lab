package com.example.blsslab.model.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_table")
public class UserEntity {
    @Id
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String familyName;

    @NotBlank
    private String password;

    private String accessToken;

    @OneToMany(mappedBy = "user")
    private Set<HousingEntity> bookings;

    @OneToMany(mappedBy = "owner")
    private Set<HousingEntity> ownedHousings;

    @ManyToMany
    @JoinTable(name = "booking_requests", joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"), inverseJoinColumns = @JoinColumn(name = "housing_id", referencedColumnName = "id"))
    private Set<HousingEntity> bookingRequests;
}
