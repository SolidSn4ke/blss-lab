package com.example.blsslab.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.postgresql.xa.PGXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.blsslab.model.db1.repos", entityManagerFactoryRef = "db1EntityManagerFactory", transactionManagerRef = "transactionManager")
public class DB1Config {
    @Bean
    @Primary
    public DataSource db1DataSource(Environment env) {
        PGXADataSource xaDataSource = new PGXADataSource();
        xaDataSource.setUrl(env.getProperty("app.datasource.db1.url"));
        xaDataSource.setUser(env.getProperty("app.datasource.db1.username"));
        xaDataSource.setPassword(env.getProperty("app.datasource.db1.password"));

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("db1");
        ds.setXaDataSource(xaDataSource);

        return ds;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean db1EntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("db1DataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.example.blsslab.model.db1.entity")
                .persistenceUnit("db1")
                .build();
    }
}
