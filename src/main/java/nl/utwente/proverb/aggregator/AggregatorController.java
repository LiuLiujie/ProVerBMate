package nl.utwente.proverb.aggregator;


import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.article.ArticleAggregator;
import nl.utwente.proverb.aggregator.github.GitHubAggregator;
import nl.utwente.proverb.ontology.service.OntologyService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class AggregatorController {

    @Resource
    private GitHubAggregator gitHubAggregator;

    @Resource
    private ArticleAggregator articleAggregator;

    @Resource
    private OntologyService ontologyService;

    public void aggregate() {
        var resultRepo = this.aggregateGitHub();
        var resultArticle = this.aggregateArticle();
        ontologyService.write();
    }

    private Map<String, Boolean> aggregateGitHub(){
        var repoResources = ontologyService.getAllRepositories();
        var result = new HashMap<String, Boolean>(repoResources.size());
        for (var resource: repoResources){
            var url = resource.toString();
            var success = gitHubAggregator.aggregateGitHubURL(url, resource);
            result.put(resource.toString(), success);
        }
        return result;
    }

    private Map<String, Boolean> aggregateArticle(){
        var articleResources = ontologyService.getAllArticles();
        var result = new HashMap<String, Boolean>(articleResources.size());
        for (var resource: articleResources){
            var url = resource.toString();
            var success = articleAggregator.aggregateDOIURL(url, resource);
            result.put(resource.toString(), success);
        }
        return result;
    }
}
