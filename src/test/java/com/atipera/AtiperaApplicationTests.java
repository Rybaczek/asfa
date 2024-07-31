package com.atipera;

import com.atipera.api.controller.UserControllerAbility;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class AtiperaApplicationTests extends BaseIntegrationTest {
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
        githubHttpClientStub.willReturnRepositories(username, "github_stub_responses/repos.json");

        githubHttpClientStub.willReturnBranches(username, "atipera", "branches/atipera.json");
        githubHttpClientStub.willReturnBranches(username, "isaac-character-generator-application", "branches/isaac-character-generator-application.json");
        githubHttpClientStub.willReturnBranches(username, "gamemasters-organizer-bestiary-and-location-interactive-navigator", "branches/gamemasters-organizer-bestiary-and-location-interactive-navigator.json");
        githubHttpClientStub.willReturnBranches(username, "bookstore-spring-exploratory", "branches/bookstore-spring-exploratory.json");

        // WHEN
        ResponseEntity<String> result = userControllerAbility.callFindRepositories(username);

        // THEN
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertActualWithExpectedResponse(result, "expected/repositories.json");

    }

    @Test
    void shouldNotParseForkedRepository() {
        // GIVEN
        String username = "Rybaczek";
        githubHttpClientStub.willReturnRepositories(username, "github_stub_responses/fork.json");

        githubHttpClientStub.willReturnBranches(username, "atipera", "branches/atipera.json");
        githubHttpClientStub.willReturnBranches(username, "isaac-character-generator-application", "branches/isaac-character-generator-application.json");
        githubHttpClientStub.willReturnBranches(username, "gamemasters-organizer-bestiary-and-location-interactive-navigator", "branches/gamemasters-organizer-bestiary-and-location-interactive-navigator.json");
        githubHttpClientStub.willReturnBranches(username, "bookstore-spring-exploratory", "branches/bookstore-spring-exploratory.json");

        // WHEN
        ResponseEntity<String> result = userControllerAbility.callFindRepositories(username);

        // THEN
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertActualWithExpectedResponse(result, "expected/repositories.json");
    }

    @Test
    void shouldFindBranches() {
        // GIVEN
        String username = "Rybaczek";
        String repositoryName = "atipera";
        githubHttpClientStub.willReturnBranches(username, repositoryName, "branches/atipera.json");

        // WHEN
        ResponseEntity<String> result = userControllerAbility.callFindBranches(username, repositoryName);

        // THEN
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertActualWithExpectedResponse(result, "expected/branch.json");
    }

    private void assertActualWithExpectedResponse(ResponseEntity<String> result, String expectedResponseResourcePath) {
        try {
            JSONAssert.assertEquals(assertionResourceReader.readResource(expectedResponseResourcePath), result.getBody(), JSONCompareMode.STRICT);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldThrowCustomResponseFor404Error(){
        // GIVEN
        String username = "notExistingUser";

        // WHEN
        ResponseEntity<String> result = userControllerAbility.callFindRepositories(username);

        // THEN
        assertThat(result.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertActualWithExpectedResponse(result, "expected/repositories.json");
    }
}

