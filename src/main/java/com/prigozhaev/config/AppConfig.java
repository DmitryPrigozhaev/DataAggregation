package com.prigozhaev.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Application Configuration Class
 *
 * @author dprigozhaev on 04.02.2020
 */

@Configuration
public class AppConfig {

    /**
     * Create and configuring {@link RestTemplate} for dependency injection.
     * The server response timeout is set to 10 seconds.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(10L))
                .setReadTimeout(Duration.ofSeconds(10L))
                .build();
    }

}