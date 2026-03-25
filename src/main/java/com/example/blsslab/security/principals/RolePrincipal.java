package com.example.blsslab.security.principals;

import java.security.Principal;

public class RolePrincipal implements Principal {
    private String role;

    public RolePrincipal(String role) {
        this.role = role;
    }

    @Override
    public String getName() {
        return role;
    }
}
