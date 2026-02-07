package com.example.blsslab.model.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "userTable")
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
}
