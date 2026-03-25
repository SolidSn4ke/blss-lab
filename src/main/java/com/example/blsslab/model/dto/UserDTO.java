package com.example.blsslab.model.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

import com.example.blsslab.model.entity.UserEntity;
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

    public UserDTO(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.name = userEntity.getName();
        this.familyName = userEntity.getFamilyName();
        this.role = userEntity.getRole();
    }
}
