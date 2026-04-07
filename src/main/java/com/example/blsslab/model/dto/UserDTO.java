package com.example.blsslab.model.dto;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {
    String username;
    String password;
    String name;
    String familyName;
    UserRole role;
    Collection<? extends GrantedAuthority> authorities;

    public void update(UserDTO newInfo) {
        if (newInfo.username != null) {
            this.username = newInfo.username;
        }
        if (newInfo.password != null) {
            this.password = newInfo.password;
        }
        if (newInfo.name != null) {
            this.name = newInfo.name;
        }
        if (newInfo.familyName != null) {
            this.familyName = newInfo.familyName;
        }
    }
}
