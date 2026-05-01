package com.example.blsslab.service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.blsslab.model.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    @Value("${jwt.sign.key}")
    private String signKey;

    public String generateToken(UserDTO user) {
        Map<String, Object> claims = Map.of("authorities",
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")));
        return Jwts.builder().setClaims(claims).setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        return Arrays.stream(extractClaim(token, claims -> claims.get("authorities")).toString().split(","))
                .map(a -> new SimpleGrantedAuthority(a)).toList();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
