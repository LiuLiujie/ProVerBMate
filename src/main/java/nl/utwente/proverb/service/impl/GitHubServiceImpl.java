package nl.utwente.proverb.service.impl;

import lombok.NonNull;

import nl.utwente.proverb.domain.dto.github.GitHubRepoContributorDTO;
import nl.utwente.proverb.domain.dto.github.GitHubRepoDTO;
import nl.utwente.proverb.domain.dto.github.GitHubRepoIssueDTO;
import nl.utwente.proverb.domain.dto.github.GitHubUserDTO;
import nl.utwente.proverb.exceptions.InvalidResourceURLException;
import nl.utwente.proverb.service.GitHubService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static nl.utwente.proverb.domain.api.GitHubAPI.GITHUB_DOMAIN;
import static nl.utwente.proverb.domain.api.GitHubAPI.GITHUB_REPO_API;

@Service
public class GitHubServiceImpl implements GitHubService {

    @Value("${github-token}")
    private String githubToken;

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Optional<GitHubRepoDTO> getGitHubRepoDetail(String url) throws InvalidResourceURLException {

        if (!url.contains(GITHUB_DOMAIN)){
            throw new InvalidResourceURLException("Not Github URI link");
        }
        String repoRestURL = url.replace(GITHUB_DOMAIN, GITHUB_REPO_API);

        var repoDetailOpt = this.getGitHubRepo(repoRestURL);
        repoDetailOpt.ifPresent(
                dto -> {
                    //Set the repo url manually
                    dto.setRepoRestURL(repoRestURL);
                    dto.setContributors(this.getGitHubRepoContributorsWithDetail(dto));
                });
        return repoDetailOpt;
    }

    @Override
    public Optional<GitHubRepoDTO> getGitHubRepo(@NonNull String repoRestURL){

        RestTemplate restTemplate = this.getTemplateWithAuth();
        try {
            return Optional.ofNullable(restTemplate.getForObject(repoRestURL, GitHubRepoDTO.class));
        } catch (RestClientException e){
            return Optional.empty();
        }
    }

    @Override
    public List<GitHubRepoIssueDTO> getGitHubRepoIssues(@NonNull String issueRestURL,@Nullable String state, @Nullable String labels) {
        RestTemplate restTemplate = this.getTemplateWithAuth();
        String url = issueRestURL
                + (state == null ? "?state=open" : "?state="+state)
                + (labels == null ? "" : "&labels="+labels);
        try {
            var issues = Optional.ofNullable(restTemplate.getForObject(url, GitHubRepoIssueDTO[].class));
            return issues.map(issue -> new ArrayList<>(List.of(issue))).orElseGet(ArrayList::new);
        }catch (RestClientException e){
            return new ArrayList<>();
        }
    }

    @Override
    public List<GitHubRepoContributorDTO> getGitHubRepoContributors(@NonNull String contributorRestURL) {
        RestTemplate restTemplate = this.getTemplateWithAuth();
        try {
            var contributors =
                    Optional.ofNullable(restTemplate.getForObject(contributorRestURL, GitHubRepoContributorDTO[].class));
            return contributors.map(contributorDTOs -> new ArrayList<>(List.of(contributorDTOs)))
                    .orElseGet(ArrayList::new);
        } catch (RestClientException e){
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<GitHubUserDTO> getGitHubUser(@NonNull String homeRestURL){
        if (homeRestURL.isEmpty()){
            return Optional.empty();
        }
        RestTemplate restTemplate = this.getTemplateWithAuth();
        try {
            return Optional.ofNullable(restTemplate.getForObject(homeRestURL, GitHubUserDTO.class));
        } catch (RestClientException e){
            return Optional.empty();
        }
    }

    private List<GitHubRepoContributorDTO> getGitHubRepoContributorsWithDetail(GitHubRepoDTO repoDTO){
        var contributors =  this.getGitHubRepoContributors(repoDTO);
        for (GitHubRepoContributorDTO contributor : contributors){
            var user = this.getGitHubUser(contributor);
            user.ifPresent(contributor::setDetail);
        }
        return contributors;
    }

    private List<GitHubRepoContributorDTO> getGitHubRepoContributors(GitHubRepoDTO repoDTO) {
        return this.getGitHubRepoContributors(repoDTO.getContributorsRestURL());
    }

    private Optional<GitHubUserDTO> getGitHubUser(GitHubRepoContributorDTO contributor) {
        return this.getGitHubUser(contributor.getHomeRestURL());
    }

    private RestTemplate getTemplateWithAuth(){
        return this.restTemplateBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token "+ githubToken)
                .build();
    }
}
