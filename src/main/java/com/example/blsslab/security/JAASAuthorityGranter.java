package com.example.blsslab.security;

import java.security.Principal;
import java.util.Set;

import org.springframework.security.authentication.jaas.AuthorityGranter;

public class JaasAuthorityGranter implements AuthorityGranter {

    @Override
    public Set<String> grant(Principal principal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'grant'");
    }
}
