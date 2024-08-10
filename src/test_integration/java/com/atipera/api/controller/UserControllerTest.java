package com.atipera.api.controller;

import com.atipera.AssertionResourceReader;
import com.atipera.BaseIntegrationTest;
import com.atipera.http.clients.GithubHttpClientStub;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest extends BaseIntegrationTest {
    @Autowired
    private GithubHttpClientStub githubHttpClientStub;
    @Autowired
    private UserControllerAbility userControllerAbility;
    @Autowired
    private AssertionResourceReader assertionResourceReader;

    @Test
    void shouldFindRepositories() {
        // GIVEN
        String username = "Rybaczek";
        githubHttpClientStub.willReturnRepositories(username, "github_stub_responses/repositories.json");

        githubHttpClientStub.willReturnBranches(username, "atipera", "branches/atipera.json");
        githubHttpClientStub.willReturnBranches(username, "isaac-character-generator-application", "branches/isaac-character-generator-application.json");
        githubHttpClientStub.willReturnBranches(username, "gamemasters-organizer-bestiary-and-location-interactive-navigator", "branches/gamemasters-organizer-bestiary-and-location-interactive-navigator.json");
        githubHttpClientStub.willReturnBranches(username, "bookstore-spring-exploratory", "branches/bookstore-spring-exploratory.json");

        // WHEN
        ResponseEntity<String> result = userControllerAbility.callFindRepositories(username);

        // THEN
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertActualWithExpectedResponse(result.getBody(), "expected/repositories.json");
    }

    @Test
    void shouldNotConsiderForkedRepository() {
        // GIVEN
        String username = "Rybaczek";
        githubHttpClientStub.willReturnRepositories(username, "github_stub_responses/repositories-with-fork.json");

        githubHttpClientStub.willReturnBranches(username, "atipera", "branches/atipera.json");
        githubHttpClientStub.willReturnBranches(username, "isaac-character-generator-application", "branches/isaac-character-generator-application.json");
        githubHttpClientStub.willReturnBranches(username, "gamemasters-organizer-bestiary-and-location-interactive-navigator", "branches/gamemasters-organizer-bestiary-and-location-interactive-navigator.json");
        githubHttpClientStub.willReturnBranches(username, "bookstore-spring-exploratory", "branches/bookstore-spring-exploratory.json");

        // WHEN
        ResponseEntity<String> result = userControllerAbility.callFindRepositories(username);

        // THEN
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertActualWithExpectedResponse(result.getBody(), "expected/repositories.json");
    }

    @Test
    void shouldGetResponseFor404RepositoryError() {
        // GIVEN
        String nonExistingUsername = "nonExistingUsername";
        githubHttpClientStub.willReturn404ErrorResponseForRepository(nonExistingUsername, "github_stub_responses/404-repositories-not-found-response.json");

        // WHEN && THEN
        assertThatThrownBy(() -> userControllerAbility.callFindRepositories(nonExistingUsername))
                .isInstanceOf(HttpClientErrorException.class)
                .satisfies(e -> {
                    HttpClientErrorException exception = (HttpClientErrorException) e;
                    assertThat(Objects.requireNonNull(exception.getResponseHeaders()).getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
                    assertActualWithExpectedResponse(exception.getResponseBodyAsString(), "expected/404-user-not-found-response.json");
                });
    }

    @Test
    void shouldGetResponseFor404BranchError() {
        // GIVEN
        String owner = "Rybaczek";
        String repositoryWithNonExistingBranch = "repository-with-non-existing-branch";
        githubHttpClientStub.willReturnRepositories(owner, "github_stub_responses/repository-with-non-existing-branch.json");
        githubHttpClientStub.willReturn404ErrorResponseForBranch(owner, repositoryWithNonExistingBranch, "github_stub_responses/404-branches-not-found-response.json");

        // WHEN && THEN
        assertThatThrownBy(() -> userControllerAbility.callFindRepositories(owner))
                .isInstanceOf(HttpClientErrorException.class)
                .satisfies(e -> {
                    HttpClientErrorException exception = (HttpClientErrorException) e;
                    assertThat(Objects.requireNonNull(exception.getResponseHeaders()).getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(404));
                    assertActualWithExpectedResponse(exception.getResponseBodyAsString(), "expected/404-branch-not-found-response.json");
                });
    }

    @Test
    void shouldGetResponseFor403ExceededRateLimitError() {
        // GIVEN
        String owner = "Rybaczek";
        githubHttpClientStub.willReturn403ErrorResponseForRepository(owner, "github_stub_responses/403-api-rate-limit-response.json");

        // WHEN && THEN
        assertThatThrownBy(() -> userControllerAbility.callFindRepositories(owner))
                .isInstanceOf(HttpClientErrorException.Forbidden.class)
                .satisfies(e -> {
                    HttpClientErrorException exception = (HttpClientErrorException) e;
                    assertThat(Objects.requireNonNull(exception.getResponseHeaders()).getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
                    assertThat(exception.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(403));
                    assertActualWithExpectedResponse(exception.getResponseBodyAsString(), "expected/403-api-rate-limit-exceeded.json");
                });
    }

    private void assertActualWithExpectedResponse(String result, String expectedResponseResourcePath) {
        try {
            JSONAssert.assertEquals(assertionResourceReader.readResource(expectedResponseResourcePath), result, JSONCompareMode.STRICT);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

