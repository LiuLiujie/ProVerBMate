package nl.utwente.proverb.aggregator.github.service;

import lombok.NonNull;
import nl.utwente.proverb.aggregator.github.dto.GitHubOrgDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubRepoContributorDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubRepoDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubUserDTO;

import java.util.List;
import java.util.Optional;

public interface GitHubService {

    Optional<GitHubRepoDTO> getGitHubRepository(@NonNull String repoRestURL);

    List<GitHubRepoContributorDTO> getGitHubRepoContributors(@NonNull String contributorRestURL);

    Optional<GitHubUserDTO> getGitHubUser(@NonNull String homeRestURL);

    Optional<GitHubOrgDTO> getGitHubOrg(@NonNull String orgRestURL);
}
