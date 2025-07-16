package com.shipping.packagingapi.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DataSourceConfig {

    /**
     * This bean is used to load the standard spring.datasource.* properties
     * which we will then override for production.
     */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * This bean correctly configures the DataSource for a cloud environment like Render.
     * It reads the DATABASE_URL environment variable and explicitly sets the driver class,
     * ensuring Spring Boot knows it's a PostgreSQL database.
     */
    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        String databaseUrl = System.getenv("DATABASE_URL");
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();

        dataSource.setJdbcUrl(databaseUrl);
        dataSource.setDriverClassName("org.postgresql.Driver");

        return dataSource;
    }
}