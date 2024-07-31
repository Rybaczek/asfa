package com.atipera.core;

import com.atipera.http.clients.GithubHttpClient;
import com.atipera.http.clients.GithubRepositoryBranchesDto;
import com.atipera.http.clients.GithubRepositoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final GithubHttpClient githubHttpClient;

    public GithubRepositories findRepositories(String username) {
        List<GithubRepositoryDto> repositories = githubHttpClient.findRepositories(username);

        return new GithubRepositories(repositories.stream()
                .filter(dto -> dto.fork().equals(false))
                .map(dto -> new GithubRepository(dto.name(), dto.owner().login(), findBranches(username, dto.name())))
                .toList());
    }

    public GithubBranches findBranches(String owner, String repo) {
        List<GithubRepositoryBranchesDto> branches = githubHttpClient.findBranches(owner, repo);

        return new GithubBranches(branches.stream()
                .map(dto -> new GithubBranch(dto.name(), dto.commit().sha()))
                .toList());
    }
}