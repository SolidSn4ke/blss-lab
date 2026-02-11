package com.example.blsslab.model.dto;

import com.example.blsslab.model.entity.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    String username;
    String name;
    String familyName;
    String password;
    String accessToken;

    public UserDTO(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.name = userEntity.getName();
        this.familyName = userEntity.getFamilyName();
    }
}
