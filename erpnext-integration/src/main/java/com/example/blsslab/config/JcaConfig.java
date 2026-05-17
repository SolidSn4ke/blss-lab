package com.example.blsslab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jca.support.LocalConnectionFactoryBean;

import com.example.blsslab.jca.ErpNextManagedConnectionFactory;

import jakarta.resource.spi.ManagedConnectionFactory;

@Configuration
public class JcaConfig {
    @Value("${spring.erpnext.host}")
    String host;

    @Value("${spring.erpnext.port}")
    Integer port;

    @Value("${spring.erpnext.api.key}")
    String apiKey;

    @Value("${spring.erpnext.api.secret}")
    String apiSecret;

    @Bean
    ManagedConnectionFactory managedConnectionFactory() {
        ErpNextManagedConnectionFactory connectionFactory = new ErpNextManagedConnectionFactory();
        connectionFactory.setUrl(String.format("http://%s:%d", host, port));
        connectionFactory.setApiKey(apiKey);
        connectionFactory.setApiSecret(apiSecret);
        return connectionFactory;
    }

    @Bean
    LocalConnectionFactoryBean connectionFactory() {
        LocalConnectionFactoryBean factoryBean = new LocalConnectionFactoryBean();
        factoryBean.setManagedConnectionFactory(managedConnectionFactory());
        return factoryBean;
    }
}
