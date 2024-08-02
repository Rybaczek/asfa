package com.atipera.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class RestTemplateTestConfig {
    @Bean
    public RestTemplate restTemplateTest(RestTemplateBuilder builder, @Value("${server.port}") int port) {
       return builder.rootUri("http://localhost:" + port).build();
    }

}
