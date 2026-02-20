package com.example.blsslab.model.entity;

import java.util.Set;

import com.example.blsslab.model.dto.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "owner")
    private Set<HousingEntity> ownedHousings;

    @OneToMany(mappedBy = "guest")
    private Set<BookingEntity> bookingRequests;
}
