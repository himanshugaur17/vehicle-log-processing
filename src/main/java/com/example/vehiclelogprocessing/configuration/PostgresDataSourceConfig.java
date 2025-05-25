package com.example.vehiclelogprocessing.configuration;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.example.vehiclelogprocessing.configuration.PostgresDataSourceConfig.PostgresDataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;

@Profile("!test")
@EnableConfigurationProperties(PostgresDataSourceProperties.class)
public class PostgresDataSourceConfig {

    @Bean
    public DataSource dataSource(PostgresDataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.url());
        dataSource.setUsername(properties.username());
        dataSource.setPassword(properties.password());
        dataSource.setDriverClassName(properties.driverClassName());
        dataSource.setMaximumPoolSize(properties.maxPoolSize());
        dataSource.setMinimumIdle(properties.minIdle());
        return dataSource;
    }

    @ConfigurationProperties(prefix = "postgres.datasource")
    public record PostgresDataSourceProperties(String url, String username, String password, String driverClassName,
            @DefaultValue("10") int maxPoolSize, @DefaultValue("5") int minIdle) {
    }

}
