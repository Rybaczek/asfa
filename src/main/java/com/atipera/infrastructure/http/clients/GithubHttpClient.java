package com.atipera.infrastructure.http.clients;

import com.atipera.PropertiesReader;
import com.atipera.infrastructure.http.GithubApiRateLimitExceededException;
import com.atipera.infrastructure.http.GithubResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.text.MessageFormat.format;
import static org.springframework.http.RequestEntity.get;

@Component
@RequiredArgsConstructor
public class GithubHttpClient {
    private final RestTemplate githubRestTemplate;
    private static final String AUTHORIZATION_TOKEN = format("Bearer {0}", PropertiesReader.getProperty("GITHUB_API_TOKEN"));

    public List<GithubRepositoryDto> findRepositories(String username) {
        RequestEntity<Void> request = get(format("/users/{0}/repos", username))
                .accept(MediaType.parseMediaType("application/vnd.github+json"))
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                .build();

        try {
            return githubRestTemplate.exchange(request, new ParameterizedTypeReference<List<GithubRepositoryDto>>() {
            }).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GithubResourceNotFoundException(format("User with name: {0} not found", username));
            }
            if (e.getStatusCode() == HttpStatus.FORBIDDEN && e.getMessage().contains("API rate limit exceeded")) {
                throw new GithubApiRateLimitExceededException(format("Could not access User with name: {0}. API rate limit exceeded", username));
            } else {
                throw e;
            }
        }
    }

    public List<GithubRepositoryBranchesDto> findBranches(String owner, String repo) {
        RequestEntity<Void> request = get(format("/repos/{0}/{1}/branches", owner, repo))
                .accept(MediaType.parseMediaType("application/vnd.github+json"))
                .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_TOKEN)
                .build();

        try {
            return githubRestTemplate.exchange(request, new ParameterizedTypeReference<List<GithubRepositoryBranchesDto>>() {
            }).getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new GithubResourceNotFoundException(format("Branch from: {0} repository owned by: {1} not found", repo, owner));
            }
            if (e.getStatusCode() == HttpStatus.FORBIDDEN && e.getMessage().contains("API rate limit exceeded")) {
                throw new GithubApiRateLimitExceededException(format("Could not access branch from: {0} repository owned by: {1}. API rate limit exceeded", repo, owner));
            } else {
                throw e;
            }
        }
    }
}
