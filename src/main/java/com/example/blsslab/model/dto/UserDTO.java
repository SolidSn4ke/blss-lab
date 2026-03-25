package com.example.blsslab.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    String username;
    String password;
    String name;
    String familyName;
    UserRole role;
}
