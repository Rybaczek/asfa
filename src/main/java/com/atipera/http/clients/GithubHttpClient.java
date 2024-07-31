package com.atipera.http.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.text.MessageFormat.format;
import static org.springframework.http.RequestEntity.get;

@Component
@RequiredArgsConstructor
public class GithubHttpClient {
    private final RestTemplate githubRestTemplate;

    public List<GithubRepositoryDto> findRepositories(String username) {
        RequestEntity<Void> request = get(format("/users/{0}/repos", username))
                .accept(MediaType.parseMediaType("application/vnd.github+json"))
                .build();

        return githubRestTemplate.exchange(request, new ParameterizedTypeReference<List<GithubRepositoryDto>>() {
        }).getBody();
    }

    public List<GithubRepositoryBranchesDto> findBranches(String owner, String repo) {

        RequestEntity<Void> request = get(format("/repos/{0}/{1}/branches", owner, repo))
                .accept(MediaType.parseMediaType("application/vnd.github+json"))
                .build();

        return githubRestTemplate.exchange(request, new ParameterizedTypeReference<List<GithubRepositoryBranchesDto>>() {
        }).getBody();
    }
}
