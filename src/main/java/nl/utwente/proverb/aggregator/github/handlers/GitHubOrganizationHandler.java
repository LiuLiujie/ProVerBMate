package nl.utwente.proverb.aggregator.github.handlers;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.github.api.GitHubAPI;
import nl.utwente.proverb.aggregator.github.service.GitHubService;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.util.DateUtil;
import org.apache.jena.rdf.model.Resource;

import java.util.Optional;

@Log4j2
public class GitHubOrganizationHandler extends GitHubHandler{

    private final GitHubService githubService;

    private final OntologyService ontologyService;

    public GitHubOrganizationHandler(GitHubService githubService, OntologyService ontologyService) {
        this.githubService = githubService;
        this.ontologyService = ontologyService;
    }

    @Override
    public boolean handle(String url, Resource githubResource) {
        if (!isCorrespondingURL(url)){
            return handleNext(url, githubResource);
        }
        log.info("GitHub Org handler start: {}", url);
        var orgName = getOrganizationName(url);
        if (orgName.isEmpty()){
            log.error("GitHub Org get name fail, url: {}", url);
            return false;
        }
        var orgURL = "https://"+GitHubAPI.GITHUB_API_GET_ORG+"/"+orgName.get();
        var dtoOpt = this.githubService.getGitHubOrg(orgURL);
        if (dtoOpt.isEmpty()){
            log.error("GitHub Org fail, url: {}", url);
            return false;
        }
        var dto = dtoOpt.get();
        ontologyService.addUniqueProperty(githubResource, PROVERB.P_NAME, dto.getName());
        ontologyService.addUniqueProperty(githubResource, PROVERB.P_ABSTRACT, dto.getDescription());
        ontologyService.addUniqueProperty(githubResource, PROVERB.P_LAST_ACTIVITY_DATE, DateUtil.getDate(dto.getLastActivity()));
        log.info("GitHub Org handler success");
        return true;
    }

    @Override
    protected boolean isCorrespondingURL(String url) {
        return getOrganizationName(url).isPresent();
    }

    private Optional<String> getOrganizationName(String githubURL){
        String path = removeGitHubPrefix(githubURL);
        String[] sp = path.split("/");
        if (sp.length == 1){
            return Optional.of(sp[0]);
        }else{
            return Optional.empty();
        }
    }
}
