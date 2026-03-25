package com.example.blsslab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.jaas.AuthorityGranter;
import org.springframework.security.authentication.jaas.JaasAuthenticationCallbackHandler;
import org.springframework.security.authentication.jaas.JaasAuthenticationProvider;
import org.springframework.security.authentication.jaas.JaasNameCallbackHandler;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.blsslab.rest.filters.JwtFilter;
import com.example.blsslab.security.JaasAuthorityGranter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final JwtFilter jwtFilter;

    @Bean
    JaasAuthenticationProvider jaasAuthenticationProvider() {
        JaasAuthenticationProvider provider = new JaasAuthenticationProvider();
        provider.setLoginConfig(new ClassPathResource("login.conf"));
        provider.setLoginContextName("BlssLab");
        provider.setAuthorityGranters(new AuthorityGranter[] { new JaasAuthorityGranter() });
        provider.setCallbackHandlers(new JaasAuthenticationCallbackHandler[] { new JaasNameCallbackHandler(),
                new JaasPasswordCallbackHandler() });
        return provider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(jaasAuthenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
