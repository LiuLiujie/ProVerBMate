package nl.utwente.proverb.controller;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.domain.ontology.PROVERB;
import nl.utwente.proverb.service.GitHubService;
import nl.utwente.proverb.service.OntologyService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
public class RepositoryController {

    @javax.annotation.Resource
    private GitHubService gitHubService;

    @javax.annotation.Resource
    private OntologyService ontologyService;

    @javax.annotation.Resource
    private Model model;

    public boolean enrichGitHubRepoDetail(Resource gitHubResource){
        var url = gitHubResource.toString();
        log.info("Enriching repository: {}", url);
        var opt = gitHubService.getGitHubRepoDetail(url);
        if (opt.isPresent()){
            var dto = opt.get();
            ontologyService.addProperty(gitHubResource, PROVERB.P_NAME, dto.getRepoName());
            ontologyService.addProperty(gitHubResource, PROVERB.P_ABSTRACT, dto.getAbs());
            for (var contributor: dto.getContributors()){
                var person = ontologyService.createContributor(contributor.getHomeHTMLURL(), gitHubResource);
                var detail = contributor.getDetail();
                if (detail != null){
                    ontologyService.addProperty(person, PROVERB.P_NAME, detail.getName());
                    ontologyService.addProperty(person, PROVERB.P_ABSTRACT, detail.getAbs());
                }
            }
            log.info("Success");
            return true;
        }
        log.error("Enriching repository: {} fail", url);
        return false;
    }
}
