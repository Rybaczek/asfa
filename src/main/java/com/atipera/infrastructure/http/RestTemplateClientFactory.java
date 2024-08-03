package com.atipera.infrastructure.http;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Component
@RequiredArgsConstructor
public class RestTemplateClientFactory {
    private final RestTemplateBuilder builder;
    private final LogbookClientHttpRequestInterceptor interceptor;

    public RestTemplate create(RestTemplateConfigurationProperties properties) {
        return builder
                .additionalInterceptors(interceptor)
                .rootUri(properties.rootUrl())
                .build();
    }
}
