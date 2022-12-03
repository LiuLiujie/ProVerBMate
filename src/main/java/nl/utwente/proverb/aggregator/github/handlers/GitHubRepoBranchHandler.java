package nl.utwente.proverb.aggregator.github.handlers;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.github.service.GitHubService;
import nl.utwente.proverb.ontology.service.OntologyService;
import org.apache.jena.rdf.model.Resource;

import java.util.Optional;

import static nl.utwente.proverb.aggregator.github.api.GitHubAPI.GITHUB_API_GET_REPO;
import static nl.utwente.proverb.aggregator.github.api.GitHubAPI.GITHUB_DOMAIN;

@Log4j2
public class GitHubRepoBranchHandler extends GitHubRepositoryHandler{

    public GitHubRepoBranchHandler(GitHubService githubService, OntologyService ontologyService) {
        super(githubService, ontologyService);
    }

    @Override
    public boolean handle(String url, Resource githubResource) {
        if (!isCorrespondingURL(url)){
            return handleNext(url, githubResource);
        }
        //First fetch the repo info
        log.info("GitHub Repo branch handler start: {}", url);
        var repoURL = this.getRepoURL(url);
        if (repoURL.isEmpty()){
            log.error("GitHub Repo branch fetch repo url fail, url: {}", url);
            return handleNext(url, githubResource);
        }
        var optGitHubRepoDTO = this.handleRepoInfo(repoURL.get(), githubResource);
        if (optGitHubRepoDTO.isEmpty()){
            log.error("GitHub Repo branch get repofail, url: {}", url);
            return handleNext(url, githubResource);
        }
        super.handleContributor(optGitHubRepoDTO.get(), githubResource);
        super.handleOwner(optGitHubRepoDTO.get(), githubResource);
        //Then fetch specific branch info
        var branchRestURL = this.getBranchURL(url);
        if (branchRestURL.isEmpty()){
            log.error("GitHub Repo branch get branch url fail, url: {}", url);
            return handleNext(url, githubResource);
        }
        handelRepoBranch(branchRestURL.get(), githubResource);
        log.info("GitHub Repo branch handler success");
        return true;
    }

    @Override
    protected boolean isCorrespondingURL(String url) {
        return this.getBranchURL(url).isPresent();
    }

    private Optional<String> getBranchURL(String url){
        if (!url.contains("/tree/")){
            return Optional.empty();
        }
        String path = removeGitHubPrefix(url);
        String[] sp = path.split("/tree/");
        if (sp.length != 2){
            return Optional.empty();
        }
        String repoURL = "https://"+ GITHUB_API_GET_REPO+"/"+sp[0].replace(GITHUB_DOMAIN, GITHUB_API_GET_REPO);
        String branchName = sp[1].split("/")[0];
        return Optional.of(repoURL + "/branches/" + branchName);
    }

    private Optional<String> getRepoURL(String url) {
        String path = removeGitHubPrefix(url);
        String[] sp = path.split("/tree");
        if (sp.length != 2){
            return Optional.empty();
        }
        return Optional.of("https://"+ GITHUB_API_GET_REPO+"/"+sp[0].replace(GITHUB_DOMAIN, GITHUB_API_GET_REPO));
    }
}
