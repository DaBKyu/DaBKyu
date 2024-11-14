package com.dabkyu.dabkyu;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private final Dotenv dotenv;

    public DatabaseConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("oracle.jdbc.driver.OracleDriver")
            .url(dotenv.get("DATABASE_URL"))
            .username(dotenv.get("DATABASE_USERNAME"))
            .password(dotenv.get("DATABASE_PASSWORD"))
            .build();
    }
}
