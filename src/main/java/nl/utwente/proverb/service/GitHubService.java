package nl.utwente.proverb.service;

import nl.utwente.proverb.domain.dto.github.GitHubRepoContributorDTO;
import nl.utwente.proverb.domain.dto.github.GitHubRepoDTO;
import nl.utwente.proverb.domain.dto.github.GitHubUserDTO;
import nl.utwente.proverb.exceptions.InvalidResourceURLException;

import java.util.List;
import java.util.Optional;

public interface GitHubService {

    Optional<GitHubRepoDTO> getGitHubRepoDetail(String url) throws InvalidResourceURLException;

    Optional<GitHubRepoDTO> getGitHubRepo(String repoRestURL);

    List<GitHubRepoContributorDTO> getGitHubRepoContributors(String contributorRestURL);

    Optional<GitHubUserDTO> getGitHubUser(String homeRestURL);
}
