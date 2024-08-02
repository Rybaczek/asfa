package com.atipera.infrastructure.http;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestTemplateClientFactory {
    private final RestTemplateBuilder builder;

    public RestTemplate create(RestTemplateConfigurationProperties properties) {
        return builder
                .rootUri(properties.rootUrl())
                .build();
    }
}
