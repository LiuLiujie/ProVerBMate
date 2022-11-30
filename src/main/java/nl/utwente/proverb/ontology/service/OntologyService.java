package nl.utwente.proverb.ontology.service;


import lombok.NonNull;
import nl.utwente.proverb.evaluator.dto.QueryResult;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

public interface OntologyService {

    void addProperty(Resource resource, Property property,@Nullable String literal);

    void addProperty(Resource resource, Property property,@NonNull Resource object);

    void addSameAs(Resource domain, Resource range);

    Resource createContributor(String githubHTMLURL, Resource gitHubResource);

    Resource createWriter(Resource articleResource, String name);

    List<Resource> getAllRepositories();

    List<Resource> getAllArticles();

    QueryResult executeSPARQLQuery(Query query);

    void write();
}
