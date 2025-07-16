package com.shipping.packagingapi.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DataSourceConfig {

    /**
     * This bean correctly configures the DataSource for a cloud environment like Render
     * that provides a single DATABASE_URL environment variable.
     * It parses the URL and sets the JDBC URL, username, and password correctly.
     */
    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        String databaseUrl = System.getenv("DATABASE_URL");
        return properties.initializeDataSourceBuilder()
                .url(databaseUrl)
                .build();
    }
}