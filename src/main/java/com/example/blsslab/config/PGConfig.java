package com.example.blsslab.config;

import java.util.HashMap;

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
@EnableJpaRepositories(basePackages = "com.example.blsslab.model.postgres.repos", entityManagerFactoryRef = "PgEntityManagerFactory", transactionManagerRef = "transactionManager")
public class PGConfig {
    @Bean
    @Primary
    public DataSource PgDataSource(Environment env) {
        PGXADataSource xaDataSource = new PGXADataSource();
        xaDataSource.setUrl(env.getProperty("app.datasource.postgres.url"));
        xaDataSource.setUser(env.getProperty("app.datasource.postgres.username"));
        xaDataSource.setPassword(env.getProperty("app.datasource.postgres.password"));

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("PgDataSource");
        ds.setXaDataSource(xaDataSource);

        return ds;
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean PgEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("PgDataSource") DataSource dataSource) {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "create");

        properties.put("hibernate.transaction.jta.platform",
                "org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform");
        properties.put("javax.persistence.transactionType", "JTA");

        return builder
                .dataSource(dataSource)
                .packages("com.example.blsslab.model.postgres.entity")
                .persistenceUnit("PostgresPU")
                .properties(properties)
                .build();
    }
}
