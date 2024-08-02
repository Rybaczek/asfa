package com.atipera.infrastructure.http.clients;


public record GithubRepositoryDto(String name, OwnerDto owner, Boolean fork) {
}
