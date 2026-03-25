package com.example.blsslab.rest.filters;

import java.io.IOException;
import java.util.Collection;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.blsslab.service.JwtService;
import com.example.blsslab.service.XmlUserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    final JwtService jwtService;

    final XmlUserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HEADER_NAME);

        if (header == null || !StringUtils.startsWithIgnoreCase(header, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        header = header.substring(BEARER_PREFIX.length());
        String username = jwtService.extractUsername(header);
        Collection<? extends GrantedAuthority> authorities = jwtService.extractAuthorities(header);

        if (SecurityContextHolder.getContext().getAuthentication() == null && userService.checkIfPresent(username)) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null,
                    authorities);

            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }
}
