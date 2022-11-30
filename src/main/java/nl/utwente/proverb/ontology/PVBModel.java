package nl.utwente.proverb.ontology;

import nl.utwente.proverb.analyzer.pvbanalyzer.entities.Repository;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A model for ProVerB RDF model
 */
public class PVBModel {

    private final Model model;

    public PVBModel(File modelFile){
        this.model = ModelFactory.createDefaultModel().read(modelFile.getAbsolutePath());
    }

    public List<Resource> getAllTools() {
        var p = model.listSubjectsWithProperty(RDF.type, PROVERB.R_TOOL);
        return p.toList();
    }

    public List<RDFNode> getProperties(Resource resource, Property property) {
        return model.listObjectsOfProperty(resource, property).toList();
    }

    public Optional<String> getProperty(Resource resource, Property property) {
        var p = model.listObjectsOfProperty(resource, property).toList().stream().findFirst();
        return p.map(rdfNode -> rdfNode.asLiteral().getString());
    }

    public Resource getTool(String insName) {
        return model.getResource(PROVERB.getURI() + insName);
    }

    public List<RDFNode> getRepositoryNodes(Resource toolResource) {
        return this.getProperties(toolResource, PROVERB.P_REPOSITORY);
    }

    public List<Repository> getRepositories(Resource toolResource) {
        var repositoryNodes = this.getRepositoryNodes(toolResource);
        var repos = new ArrayList<Repository>(repositoryNodes.size());
        for (var node : repositoryNodes){
            var dto = new Repository();
            dto.setUrl(node.toString());
            this.getProperty((Resource) node, PROVERB.P_NAME).ifPresent(dto::setName);
            this.getProperty((Resource) node, PROVERB.P_LAST_COMMIT_DATE).ifPresent(dto::setLastCommitDate);
            repos.add(dto);
        }
        return repos;
    }

    public List<Resource> getAllRepositories() {
        var p = model.listSubjectsWithProperty(RDF.type, PROVERB.R_REPOSITORY);
        return p.toList();
    }
}
