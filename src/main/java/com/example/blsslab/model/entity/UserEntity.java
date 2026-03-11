package com.example.blsslab.model.entity;

import java.util.Set;

import com.example.blsslab.model.dto.UserDTO;
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
    @NotBlank
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    private String familyName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(mappedBy = "owner")
    private Set<HousingEntity> ownedHousings;

    @OneToMany(mappedBy = "guest")
    private Set<BookingEntity> bookingRequests;

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Username can contain only Latin letters, numbers, and a symbol '_'");
        }
        this.username = username;
    }

    public void update(UserDTO userDTO) {
        if (userDTO.getName() != null) {
            this.name = userDTO.getName();
        }
        if (userDTO.getFamilyName() != null) {
            this.familyName = userDTO.getFamilyName();
        }
        if (userDTO.getRole() != null) {
            this.role = userDTO.getRole();
        }
    }
}
