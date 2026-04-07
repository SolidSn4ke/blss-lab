package com.example.blsslab.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.blsslab.model.mysql.repos", entityManagerFactoryRef = "MySQLEntityManagerFactory", transactionManagerRef = "transactionManager")
public class MySQLConfig {

    @Bean
    public DataSource MySQLDataSource(Environment env) {
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUrl(env.getProperty("app.datasource.mysql.url"));
        xaDataSource.setUser(env.getProperty("app.datasource.mysql.username"));
        xaDataSource.setPassword(env.getProperty("app.datasource.mysql.password"));

        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("MySQLDataSource");
        ds.setXaDataSource(xaDataSource);

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean MySQLEntityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("MySQLDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.example.blsslab.model.mysql.entity")
                .persistenceUnit("MySQLPU")
                .build();
    }
}
