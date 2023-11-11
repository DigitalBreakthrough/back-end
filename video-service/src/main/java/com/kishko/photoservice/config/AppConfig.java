package com.kishko.photoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(value = "myConfig")
public class AppConfig {

    @Bean
    public WebClient webMLClient() {
        return WebClient.builder().baseUrl("http://localhost:8083").build();
    }

}
