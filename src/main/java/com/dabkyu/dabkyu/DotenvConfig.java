package com.dabkyu.dabkyu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenf() {
        return Dotenv.configure().ignoreIfMissing().load();
    }
}
