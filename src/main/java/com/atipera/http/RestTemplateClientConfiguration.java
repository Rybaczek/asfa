package com.atipera.http;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateClientConfiguration {

    private final RestTemplateClientFactory restTemplateClientFactory;

    @Bean
    public RestTemplate githubRestTemplate(RestTemplateConfigurationProperties properties){
        return restTemplateClientFactory.create(properties);
    }
}
