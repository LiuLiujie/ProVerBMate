package nl.utwente.proverb.orchestrator;

import org.springframework.context.annotation.Configuration;

@Configuration
public class OrchestratorConfiguration {

    boolean enableAggregator = true;

    boolean enableMDDataExtraction = true;

    boolean enableMDDataWriteBack = true;

    boolean enableEvaluator = false;
}
