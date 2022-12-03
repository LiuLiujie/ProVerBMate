package nl.utwente.proverb.orchestrator;

import nl.utwente.proverb.aggregator.AggregatorController;
import nl.utwente.proverb.analyzer.pvbanalyzer.PVBController;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class Orchestrator {

    OrchestratorConfiguration config;

    @Resource
    AggregatorController aggregatorController;

    @Resource
    PVBController pvbController;

    @PostConstruct
    public void autoOrchestrate(){

        if (this.config.enableMDDataExtraction){
            pvbController.extractInfo();
        }

        if (this.config.enableAggregator){
            aggregatorController.aggregate();
        }

        if (this.config.enableMDDataWriteBack){
            pvbController.enrichment();
        }
    }
}
