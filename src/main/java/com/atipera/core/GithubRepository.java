package com.atipera.core;

import java.util.List;

public record GithubRepository(String repositoryName, String ownerLogin, List<GithubBranch> branches){}
