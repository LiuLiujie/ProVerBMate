package nl.utwente.proverb.ontology.service;


import lombok.NonNull;
import nl.utwente.proverb.evaluator.dto.QueryResult;
import org.apache.jena.query.Query;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface OntologyService {

    void addProperty(Resource resource, Property property,@Nullable String literal);

    void addUniqueProperty(Resource resource, Property property, @Nullable String literal);

    void addProperty(Resource resource, Property property,@NonNull Resource object);

    void addSameAs(Resource domain, Resource range);

    Resource createTool(String toolName);

    Resource createRepository(String githubURL);
    
    Resource createArticle(String doi);

    Resource createContributor(String githubHTMLURL, Resource gitHubResource);

    Resource createWriter(Resource articleResource, String name);

    Optional<String> getProperty(Resource resource, Property property);

    List<RDFNode> getProperties(Resource resource, Property property);

    List<Resource> getAllRepositories();

    List<Resource> getAllArticles();

    Resource getToolResource(String toolName);

    QueryResult executeSPARQLQuery(Query query);

    void write();
}
