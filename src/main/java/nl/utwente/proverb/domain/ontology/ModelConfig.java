package nl.utwente.proverb.domain.ontology;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Value("${model.proverb}")
    private String filePath;

    @Bean
    public Model loadProVerBModel() {
        var model = ModelFactory.createDefaultModel();
        model.read(filePath);
        return model;
    }
}
