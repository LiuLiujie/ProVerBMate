package nl.utwente.proverb.aggregator.github.service;

import lombok.NonNull;
import nl.utwente.proverb.aggregator.github.dto.*;

import java.util.List;
import java.util.Optional;

public interface GitHubService {

    Optional<GitHubRepoDTO> getGitHubRepository(@NonNull String repoRestURL);

    Optional<GitHubBranchDTO> getGitHubBranch(@NonNull String branchRestURL);

    List<GitHubRepoContributorDTO> getGitHubRepoContributors(@NonNull String contributorRestURL);

    Optional<GitHubUserDTO> getGitHubUser(@NonNull String homeRestURL);

    Optional<GitHubOrgDTO> getGitHubOrg(@NonNull String orgRestURL);
}
