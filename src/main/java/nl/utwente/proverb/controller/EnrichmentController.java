package nl.utwente.proverb.controller;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.service.OntologyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
public class EnrichmentController {

    @Value("${model.proverb}")
    private String oldFileName;

    @Resource
    private RepositoryController repositoryController;

    @Resource
    private ArticleController articleController;

    @Resource
    private OntologyService ontologyService;

    public void enrich() {
        log.info("Enrichment start");

        log.info("Enrich Repository");
        var repoResult = this.enrichRepository();

        log.info("Enrich Articles");
        var artiResult = this.enrichArticles();
        log.info("Enrichment end");

        log.info("Start to write file");
        this.writeFile();

        log.info("Generate matrices");
        int repoNum = repoResult.size();
        int repoSuccess = this.getSuccessNum(repoResult);
        log.info("{} repositories in total, {} Success", repoNum, repoSuccess);

        int artiNum = artiResult.size();
        int artiSuccess = this.getSuccessNum(artiResult);
        log.info("{} articles in total, {} Success", artiNum, artiSuccess);
        log.info("All jobs succeed");
    }


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
            if (oldFileName.contains("extracted")){
                var filename = oldFileName.replace("extracted", "enriched");
                ontologyService.write(filename);
            }else {
                ontologyService.write("enriched_ProVerB.owl");
            }
        }catch (IOException e){
            log.error("Write to file fail");
        }
    }

    private int getSuccessNum(Map<String, Boolean> map){
        var keys = map.keySet();
        int success = 0;
        for (var key : keys){
            if (Boolean.TRUE.equals(map.get(key))){
                success++;
            }
        }
        return success;
    }
}
