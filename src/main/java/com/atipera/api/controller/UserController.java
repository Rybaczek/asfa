package com.atipera.api.controller;

import com.atipera.core.GithubRepositories;
import com.atipera.core.GithubBranches;
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
    //obsługa co się wydarzy gdy używkotnik ma więcej repo niż ustawiony deafault na stronę

    private final GithubService githubService;

    @GetMapping("{username}/repositories")
    public GithubRepositories findRepositories(@PathVariable String username){
        return githubService.findRepositories(username);
    }

    @GetMapping("{owner}/{name}/branches")
    public GithubBranches findBranches(@PathVariable String owner, @PathVariable String name){
        return githubService.findBranches(owner,name);
    }
}
