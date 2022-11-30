package nl.utwente.proverb.aggregator.github;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.github.handlers.GitHubHandler;
import nl.utwente.proverb.aggregator.github.handlers.GitHubOrganizationHandler;
import nl.utwente.proverb.aggregator.github.handlers.GitHubRepositoryHandler;
import nl.utwente.proverb.aggregator.github.service.GitHubService;
import nl.utwente.proverb.ontology.service.OntologyService;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class GitHubAggregator {

    @javax.annotation.Resource
    private GitHubService githubService;

    @javax.annotation.Resource
    private OntologyService ontologyService;

    public boolean aggregateGitHubURL(String url, Resource githubResource){

        var handler = GitHubHandler.setHandlerChain(
                new GitHubOrganizationHandler(this.githubService, this.ontologyService),
                new GitHubRepositoryHandler(this.githubService, this.ontologyService)
        );
        return handler.handle(url, githubResource);
    }
}
