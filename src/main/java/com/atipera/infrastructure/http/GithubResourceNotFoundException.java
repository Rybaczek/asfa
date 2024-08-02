package com.atipera.infrastructure.http;

public class GithubResourceNotFoundException extends RuntimeException {
    public GithubResourceNotFoundException(String message){
        super(message);
    }

}
