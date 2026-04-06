package com.example.blsslab.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.postgresql.xa.PGXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.blsslab.model.db2.repos", entityManagerFactoryRef = "db2EntityManagerFactory", transactionManagerRef = "transactionManager")
public class DB2Config {

    @Bean
    public DataSource db2DataSource(Environment env) {
        PGXADataSource xaDataSource = new PGXADataSource();
        xaDataSource.setUrl(env.getProperty("app.datasource.db2.url"));
        xaDataSource.setUser(env.getProperty("app.datasource.db2.username"));
        xaDataSource.setPassword(env.getProperty("app.datasource.db2.password"));

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("db2");
        ds.setXaDataSource(xaDataSource);

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean db2EntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("db2DataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.example.blsslab.model.db2.entity")
                .persistenceUnit("db2")
                .build();
    }
}
