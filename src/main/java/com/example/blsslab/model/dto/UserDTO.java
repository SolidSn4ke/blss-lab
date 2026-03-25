package com.example.blsslab.model.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

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
    Collection<? extends GrantedAuthority> authorities;
}
