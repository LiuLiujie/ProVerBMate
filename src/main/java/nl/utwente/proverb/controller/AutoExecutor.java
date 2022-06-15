package nl.utwente.proverb.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Log4j2
@Profile("daily")
@Component
public class AutoExecutor {

    @Resource
    private EnrichmentController enrichmentController;

    @PostConstruct
    public void execute(){
        log.info("Enrichment start");

        log.info("Enrich Repository");
        enrichmentController.enrichRepository();
        log.info("Enrich Articles");
        enrichmentController.enrichArticles();
        log.info("Enrichment end");

        log.info("Start to write file");
        enrichmentController.writeFile();
        log.info("All jobs succeed");
    }
}
