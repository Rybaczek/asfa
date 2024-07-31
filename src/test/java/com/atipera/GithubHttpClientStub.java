package com.atipera;

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
        stubFor(get(urlPathEqualTo(format("/users/{0}/repos", username)))
                .withHeader(HttpHeaders.ACCEPT, equalTo("application/vnd.github+json"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }

    public void willReturnBranches(String owner, String repo, String resourcePath) {
        stubFor(get(urlPathEqualTo(format("/repos/{0}/{1}/branches", owner, repo)))
                .withHeader(HttpHeaders.ACCEPT, equalTo("application/vnd.github+json"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                        .withBody(assertionResourceReader.readResource(resourcePath)))
        );
    }
}
