package atipera.core;

import com.atipera.core.GithubService;
import com.atipera.infrastructure.http.clients.GithubHttpClient;
import com.atipera.infrastructure.http.clients.GithubRepositoryDto;
import com.atipera.infrastructure.http.clients.OwnerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {
    @InjectMocks
    private GithubService githubService;

    @Mock
    private GithubHttpClient githubHttpClient;

    @Test
    void shouldFindBranchesForAllNotForkedRepositories() {
        // GIVEN
        List<GithubRepositoryDto> githubRepositories = List.of(
                new GithubRepositoryDto("repository_name_1", new OwnerDto("owner_login_1"), false),
                new GithubRepositoryDto("repository_name_2", new OwnerDto("owner_login_2"), false),
                new GithubRepositoryDto("repository_name_3", new OwnerDto("owner_login_3"), false),
                new GithubRepositoryDto("repository_name_4", new OwnerDto("owner_login_4"), false),
                new GithubRepositoryDto("repository_name_5", new OwnerDto("owner_login_5"), false)
        );
        given(githubHttpClient.findRepositories("Rybaczek"))
                .willReturn(githubRepositories);

        // WHEN
        githubService.findRepositories("Rybaczek");

        // THEN
        then(githubHttpClient)
                .should(times(5))
                .findBranches(anyString(), anyString());
    }

    @Test
    void shouldNotFindBranchesForForkedRepositories() {
        // GIVEN
        List<GithubRepositoryDto> githubRepositories = List.of(
                new GithubRepositoryDto("repository_name_1", new OwnerDto("owner_login_1"), true),
                new GithubRepositoryDto("repository_name_2", new OwnerDto("owner_login_2"), false),
                new GithubRepositoryDto("repository_name_3", new OwnerDto("owner_login_3"), true),
                new GithubRepositoryDto("repository_name_4", new OwnerDto("owner_login_4"), false),
                new GithubRepositoryDto("repository_name_5", new OwnerDto("owner_login_5"), true)
        );
        given(githubHttpClient.findRepositories("Rybaczek"))
                .willReturn(githubRepositories);

        // WHEN
        githubService.findRepositories("Rybaczek");

        // THEN
        then(githubHttpClient)
                .should(Mockito.atMost(3))
                .findBranches(anyString(), anyString());
    }

    @Test
    void shouldNotFindBranchesForNoReturnedRepositories() {
        // GIVEN
        List<GithubRepositoryDto> githubRepositories = emptyList();
        given(githubHttpClient.findRepositories("Rybaczek"))
                .willReturn(githubRepositories);

        // WHEN
        githubService.findRepositories("Rybaczek");

        // THEN
        then(githubHttpClient)
                .should(never())
                .findBranches(anyString(), anyString());
    }
    //i love pugs
}
