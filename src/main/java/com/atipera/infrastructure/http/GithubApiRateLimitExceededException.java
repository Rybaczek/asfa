package com.atipera.infrastructure.http;

public class GithubApiRateLimitExceededException extends RuntimeException{
    public GithubApiRateLimitExceededException(String message) {
        super(message);
    }
}
