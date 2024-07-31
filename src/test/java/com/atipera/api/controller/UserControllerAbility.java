package com.atipera.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.text.MessageFormat.format;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.RequestEntity.get;

@TestComponent
@RequiredArgsConstructor
public class UserControllerAbility {
    private final RestTemplate restTemplateTest;

    public ResponseEntity<String> callFindRepositories(String username) {
        RequestEntity<Void> request = get(format("/users/{0}/repositories", username))
                .accept(APPLICATION_JSON)
                .build();

        return restTemplateTest.exchange(request, String.class);
    }

    public ResponseEntity<String> callFindBranches(String username, String repositoryName) {
        RequestEntity<Void> request = get(format("/users/{0}/{1}/branches", username, repositoryName))
                .accept(APPLICATION_JSON)
                .build();

        return restTemplateTest.exchange(request, String.class);
    }
}
