package com.example.vehiclelogprocessing.jdbc.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class RawDataSourceFactory {
    private static final DataSource dataSource;

    static {
        Properties props = new Properties();
        try (InputStream input = RawDataSourceFactory.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            props.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties", e);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.user"));
        config.setPassword(props.getProperty("db.password"));
        config.setDriverClassName(props.getProperty("db.driver"));
        config.setPoolName(props.getProperty("db.poolName"));

        // perf tuning
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.maxPoolSize", "10")));
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes
        dataSource = new HikariDataSource(config);
    }

    private RawDataSourceFactory() {

    }

    public static DataSource getRawDataSource() {
        return dataSource;
    }
}
