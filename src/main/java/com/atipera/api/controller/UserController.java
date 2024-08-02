package com.atipera.api.controller;

import com.atipera.core.GithubRepositories;
import com.atipera.core.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final GithubService githubService;

    @GetMapping("{username}/repositories")
    public GithubRepositories findRepositories(@PathVariable String username) {
        return githubService.findRepositories(username);
    }
}
