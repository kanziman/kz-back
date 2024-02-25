package net.kanzi.kz.config;

import com.querydsl.core.annotations.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DatabaseProperties databaseProperties;
//    @Autowired
//    private DataSource dataSource;
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url(databaseProperties.getUrl())
                .username(databaseProperties.getUsername())
                .password(databaseProperties.getPassword())
                .driverClassName(databaseProperties.getDriverClassName())
                .build();
    }
//    public Connection getConnection() throws SQLException {
//        return dataSource.getConnection();
//    }
}
