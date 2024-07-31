package com.atipera.core;

public record GithubRepository(String repositoryName, String ownerLogin, GithubBranches branches){}
