package com.example.blsslab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jca.support.LocalConnectionFactoryBean;

import com.example.blsslab.jca.ErpNextManagedConnectionFactory;

import jakarta.resource.spi.ManagedConnectionFactory;

@Configuration
public class JcaConfig {
    @Bean
    ManagedConnectionFactory managedConnectionFactory() {
        ErpNextManagedConnectionFactory connectionFactory = new ErpNextManagedConnectionFactory();
        connectionFactory.setUrl("http://localhost:8080");
        connectionFactory.setApiKey("aabe94341273b2d");
        connectionFactory.setApiSecret("60e8ef383a39abb");
        return connectionFactory;
    }

    @Bean
    LocalConnectionFactoryBean connectionFactory() {
        LocalConnectionFactoryBean factoryBean = new LocalConnectionFactoryBean();
        factoryBean.setManagedConnectionFactory(managedConnectionFactory());
        return factoryBean;
    }
}
