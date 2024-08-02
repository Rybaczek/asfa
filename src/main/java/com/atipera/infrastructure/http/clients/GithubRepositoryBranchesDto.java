package com.atipera.infrastructure.http.clients;

public record GithubRepositoryBranchesDto(String name, CommitDto commit) {
}
