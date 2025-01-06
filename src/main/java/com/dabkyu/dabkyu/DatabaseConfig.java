package com.dabkyu.dabkyu;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("oracle.jdbc.driver.OracleDriver")
            .url("jdbc:oracle:thin:@//112.154.193.5:1521/dabkyupdb")
            .username("dabkyudev")
            .password("12345")
            .build();
    }
}
