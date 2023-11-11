package ru.kishko.photoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(value = "myConfig")
public class AppConfig {

    @Bean
    public WebClient webMLClient() {
        return WebClient.builder().baseUrl("http://127.0.0.1:8000").build();
    }

}