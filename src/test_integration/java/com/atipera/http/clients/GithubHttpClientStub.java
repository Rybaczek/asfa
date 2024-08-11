package com.atipera.http.clients;

import com.atipera.AssertionResourceReader;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpHeaders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.text.MessageFormat.format;


@TestComponent
public class GithubHttpClientStub {
    @Autowired
    private AssertionResourceReader assertionResourceReader;

    public void willReturnRepositories(String username, String resourcePath) {
        stubFor(userRepositoriesEndpoint(username)
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }


    public void willReturnBranches(String owner, String repo, String resourcePath) {
        stubFor(userRepositoryBranchesEndpoint(owner, repo)
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }

    public void willReturn404ErrorResponseForRepository(String username, String resourcePath) {
        stubFor(userRepositoriesEndpoint(username)
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withStatus(404)
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }

    public void willReturn403ErrorResponseForRepository(String username, String resourcePath) {
        stubFor(userRepositoriesEndpoint(username)
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withStatus(403)
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }

    public void willReturn404ErrorResponseForBranch(String owner, String repo, String resourcePath) {
        stubFor(userRepositoryBranchesEndpoint(owner, repo)
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .withStatus(404)
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }

    private MappingBuilder userRepositoriesEndpoint(String username) {
        return get(urlPathEqualTo(format("/users/{0}/repos", username)))
                .withHeader(HttpHeaders.ACCEPT, equalTo("application/vnd.github+json"));
    }

    private MappingBuilder userRepositoryBranchesEndpoint(String owner, String repo) {
        return get(urlPathEqualTo(format("/repos/{0}/{1}/branches", owner, repo)))
                .withHeader(HttpHeaders.ACCEPT, equalTo("application/vnd.github+json"));
    }
}
