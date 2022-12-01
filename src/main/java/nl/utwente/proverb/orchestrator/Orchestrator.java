package nl.utwente.proverb.orchestrator;

import nl.utwente.proverb.aggregator.AggregatorController;
import nl.utwente.proverb.analyzer.pvbanalyzer.PVBController;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class Orchestrator {

    @Resource
    AggregatorController aggregatorController;

    @Resource
    PVBController pvbController;

    @PostConstruct
    public void autoOrchestrate(){

        aggregatorController.aggregate();
        pvbController.autoEnrichment();
    }
}
