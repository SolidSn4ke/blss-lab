package com.example.blsslab.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.example.blsslab.model.repos")
public class PersistenceConfig {
    
}
