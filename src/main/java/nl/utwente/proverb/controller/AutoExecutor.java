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
       enrichmentController.enrich();
    }
}
