package com.shipping.packagingapi.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("prod")
public class DataSourceConfig {

    /**
     * This bean is responsible for loading the standard spring.datasource.*
     * properties from environment variables or property files.
     * The @Primary annotation gives it precedence.
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * This bean explicitly creates the production DataSource.
     * It uses the properties loaded above but constructs the URL in the exact format
     * needed by JDBC, using Render's individual environment variables.
     */
    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        String url = String.format("jdbc:postgresql://%s:%s/%s",
                System.getenv("DB_HOST"),
                System.getenv("DB_PORT"),
                System.getenv("DB_DATABASE"));

        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .url(url)
                .username(System.getenv("DB_USER"))
                .password(System.getenv("DB_PASS"))
                .build();
    }
}