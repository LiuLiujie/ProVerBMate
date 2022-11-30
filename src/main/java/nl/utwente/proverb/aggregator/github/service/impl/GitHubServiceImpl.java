package nl.utwente.proverb.aggregator.github.service.impl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.github.dto.GitHubOrgDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubRepoContributorDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubRepoDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubUserDTO;
import nl.utwente.proverb.aggregator.github.service.GitHubService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class GitHubServiceImpl implements GitHubService {

    @Value("${github-token}")
    private String githubToken;

    @javax.annotation.Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Optional<GitHubRepoDTO> getGitHubRepository(@NonNull String repoRestURL) {
        try {
            return Optional.ofNullable(this.getTemplateWithAuth().getForObject(repoRestURL, GitHubRepoDTO.class));
        } catch (RestClientException e){
            return Optional.empty();
        }
    }

    @Override
    public List<GitHubRepoContributorDTO> getGitHubRepoContributors(@NonNull String contributorRestURL) {
        try {
            var contributors =
                    Optional.ofNullable(this.getTemplateWithAuth().getForObject(contributorRestURL, GitHubRepoContributorDTO[].class));
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
        try {
            return Optional.ofNullable(this.getTemplateWithAuth().getForObject(homeRestURL, GitHubUserDTO.class));
        } catch (RestClientException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<GitHubOrgDTO> getGitHubOrg(@NonNull String orgRestURL) {
        try {
            return Optional.ofNullable(this.getTemplateWithAuth().getForObject(orgRestURL, GitHubOrgDTO.class));
        }catch (RestClientException e){
            log.error("Get Org Fail: " + e.getMessage());
            return Optional.empty();
        }
    }

    private RestTemplate getTemplateWithAuth(){
        return this.restTemplateBuilder
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token "+ githubToken)
                .build();
    }
}
