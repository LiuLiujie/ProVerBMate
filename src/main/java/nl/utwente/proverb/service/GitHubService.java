package nl.utwente.proverb.service;

import lombok.NonNull;
import nl.utwente.proverb.domain.dto.github.GitHubRepoContributorDTO;
import nl.utwente.proverb.domain.dto.github.GitHubRepoDTO;
import nl.utwente.proverb.domain.dto.github.GitHubRepoIssueDTO;
import nl.utwente.proverb.domain.dto.github.GitHubUserDTO;
import nl.utwente.proverb.exceptions.InvalidResourceURLException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface GitHubService {

    Optional<GitHubRepoDTO> getGitHubRepoDetail(String url) throws InvalidResourceURLException;

    Optional<GitHubRepoDTO> getGitHubRepo(String repoRestURL);

    List<GitHubRepoIssueDTO> getGitHubRepoIssues(@NonNull String issueRestURL, @Nullable String state, @Nullable String labels);

    List<GitHubRepoContributorDTO> getGitHubRepoContributors(String contributorRestURL);

    Optional<GitHubUserDTO> getGitHubUser(String homeRestURL);
}
