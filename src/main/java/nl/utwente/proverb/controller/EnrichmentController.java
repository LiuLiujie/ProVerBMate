package nl.utwente.proverb.controller;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.service.OntologyService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class EnrichmentController {

    @Resource
    private RepositoryController repositoryController;

    @Resource
    private ArticleController articleController;

    @Resource
    private OntologyService ontologyService;


    public Map<String, Boolean> enrichRepository(){
        var repoResources = ontologyService.getAllRepositories();
        var result = new HashMap<String, Boolean>(repoResources.size());
        repoResources.forEach(resource ->{
            var success = repositoryController.enrichGitHubRepoDetail(resource);
            var uri = resource.toString();
            result.put(uri, success);
        });
        return result;
    }

    public Map<String, Boolean> enrichArticles() {
        var articleResources = ontologyService.getAllArticles();
        var result = new HashMap<String, Boolean>(articleResources.size());
        articleResources.forEach(resource -> {
            var success = articleController.enrichArticle(resource);
            var uri = resource.toString();
            result.put(uri, success);
        });
        return result;
    }

    public void writeFile(){
        try {
            ontologyService.write();
        }catch (IOException e){
            log.error("Write to file fail");
        }
    }

    @PostConstruct
    void execution(){
        log.info("Enrichment start");

        log.info("Enrich Repository");
        this.enrichRepository();
        log.info("Enrich Articles");
        this.enrichArticles();
        log.info("Enrichment end");

        log.info("Start to write file");
        this.writeFile();
        log.info("All jobs succeed");
    }
}
