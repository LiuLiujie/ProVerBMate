package nl.utwente.proverb.aggregator.github.handlers;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.github.dto.GitHubBranchDTO;
import nl.utwente.proverb.aggregator.github.dto.GitHubRepoDTO;
import nl.utwente.proverb.aggregator.github.service.GitHubService;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.util.DateUtil;
import org.apache.jena.rdf.model.Resource;

import java.util.Optional;

import static nl.utwente.proverb.aggregator.github.api.GitHubAPI.GITHUB_API_GET_REPO;
import static nl.utwente.proverb.aggregator.github.api.GitHubAPI.GITHUB_DOMAIN;

@Log4j2
public class GitHubRepositoryHandler extends GitHubHandler{

    private final GitHubService githubService;

    private final OntologyService ontologyService;

    public GitHubRepositoryHandler(GitHubService githubService, OntologyService ontologyService) {
        this.githubService = githubService;
        this.ontologyService = ontologyService;
    }

    @Override
    public boolean handle(String url, Resource githubResource) {
        if (!isCorrespondingURL(url)){
            return handleNext(url, githubResource);
        }
        log.info("GitHub Repo handler start: {}", url);
        var repoRestURL = url.replace(GITHUB_DOMAIN, GITHUB_API_GET_REPO);
        var optGitHubRepoDTO = handleRepoInfo(repoRestURL, githubResource);
        if (optGitHubRepoDTO.isEmpty()){
            log.error("GitHub Repo fail, url: {}", url);
            return handleNext(url, githubResource);
        }
        var repoDTO = optGitHubRepoDTO.get();
        handleContributor(repoDTO, githubResource);
        var branchRestURL = repoRestURL + "/branches/"+repoDTO.getDefaultBranch();
        handelRepoBranch(branchRestURL, githubResource);
        log.info("GitHub Repo handler success");
        return true;
    }

    @Override
    protected boolean isCorrespondingURL(String url) {
        String path = removeGitHubPrefix(url);
        String[] sp = path.split("/");
        return sp.length == 2;
    }

    protected Optional<GitHubRepoDTO> handleRepoInfo(String repoRestURL, Resource githubResource) {

        var dtoOpt = this.githubService.getGitHubRepository(repoRestURL);
        if (dtoOpt.isEmpty()){
            return Optional.empty();
        }
        var dto = dtoOpt.get();
        dto.setRepoRestURL(repoRestURL);
        this.ontologyService.addUniqueProperty(githubResource, PROVERB.P_NAME, dto.getRepoName());
        this.ontologyService.addUniqueProperty(githubResource, PROVERB.P_ABSTRACT, dto.getAbs());
        this.ontologyService.addUniqueProperty(githubResource, PROVERB.P_LAST_ACTIVITY_DATE, DateUtil.getDate(dto.getLastCommit()));
        return dtoOpt;
    }

    protected Optional<GitHubBranchDTO> handelRepoBranch(String branchRestURL, Resource githubResource){
        var dtoOpt = this.githubService.getGitHubBranch(branchRestURL);
        if (dtoOpt.isEmpty()){
            return Optional.empty();
        }
        var dto = dtoOpt.get();
        var lastCommitTime = dto.getCommit().getCommitInfo().getAuthor().getDate();
        this.ontologyService.addUniqueProperty(githubResource, PROVERB.P_LAST_COMMIT_DATE, DateUtil.getDate(lastCommitTime));
        return dtoOpt;
    }

    protected void handleContributor(GitHubRepoDTO repoDTO, Resource githubResource) {
        var dtoOpt = this.githubService.getGitHubRepoContributors(repoDTO.getContributorsRestURL());
        if (dtoOpt.isEmpty()){
            return;
        }
        for (var contributor : dtoOpt){
            var user = this.githubService.getGitHubUser(contributor.getHomeRestURL());
            user.ifPresent(contributor::setDetail);
        }
        for (var contributor: dtoOpt){
            var person = this.ontologyService.createContributor(contributor.getHomeHTMLURL(), githubResource);
            var detail = contributor.getDetail();
            if (detail != null){
                this.ontologyService.addUniqueProperty(person, PROVERB.P_NAME, detail.getName());
                this.ontologyService.addUniqueProperty(person, PROVERB.P_ABSTRACT, detail.getAbs());
            }
        }
    }
}
