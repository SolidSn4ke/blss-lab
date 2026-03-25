package com.example.blsslab.security;

import com.example.blsslab.security.principals.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.jaas.AuthorityGranter;

public class JaasAuthorityGranter implements AuthorityGranter {

    @Override
    public Set<String> grant(Principal principal) {
        Set<String> roles = new HashSet<>();
        if (principal instanceof RolePrincipal) {
            roles.add(principal.getName());
        }
        return roles;
    }
}
