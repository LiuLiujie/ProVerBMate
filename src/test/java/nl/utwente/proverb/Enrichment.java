package nl.utwente.proverb;

import nl.utwente.proverb.controller.EnrichmentController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

@ActiveProfiles("daily")
@SpringBootTest
class Enrichment {

    @Resource
    EnrichmentController enrichmentController;

    @Test
    void enrichRepository(){
        enrichmentController.enrichRepository();
    }
}
