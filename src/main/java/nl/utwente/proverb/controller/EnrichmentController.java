package nl.utwente.proverb.controller;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.service.OntologyService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
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
    private OntologyService ontologyService;

    ConfigurableApplicationContext context;

    public Map<String, Boolean> enrichRepository(){
        var repoResources = ontologyService.getAllRepository();
        var result = new HashMap<String, Boolean>(repoResources.size());
        repoResources.forEach(resource ->{
            var success = repositoryController.enrichGitHubRepoDetail(resource);
            var uri = resource.toString();
            result.put(uri, success);
        });
        try {
            ontologyService.write();
        }catch (IOException e){
            log.error("Write to file fail");
        }
        return result;
    }

    @PostConstruct
    void execution(){
        log.info("Enrichment start");
        this.enrichRepository();
        log.info("Application exit");
        SpringApplication.exit(context, () -> 0);
    }
}
