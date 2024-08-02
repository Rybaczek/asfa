package com.atipera.infrastructure.http;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rest-template-client.clients.github")
public record RestTemplateConfigurationProperties(String rootUrl) {
}
