package com.atipera.http.clients;


public record GithubRepositoryDto(String name, OwnerDto owner, Boolean fork) {
}
