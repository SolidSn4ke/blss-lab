package com.example.blsslab.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PgConfig {

    @Bean
    DataSource PgDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5434/erpnext_integ")
                .username("postgres")
                .password("postgres")
                .build();
    }
}
