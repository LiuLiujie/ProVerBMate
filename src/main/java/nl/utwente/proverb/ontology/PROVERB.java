package nl.utwente.proverb.ontology;

import lombok.Getter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * ProVerB ontology
 *
 * @author Yujie
 * @version 1.4.0
 */
public class PROVERB {

    private PROVERB() { }

    @Getter
    public static final String URI = "http://slebok.github.io/proverb/ontology#";

    /*Since Ontology v1.2*/
    private static final Model m = ModelFactory.createDefaultModel();

    public static final Resource R_ARTICLE = m.createResource(URI + "Article");

    public static final Resource R_CONCEPT = m.createProperty(URI + "Concept");

    public static final Resource R_FORMAT = m.createResource(URI + "Format");

    public static final Resource R_CODE_CONTRIBUTOR = m.createResource(URI + "CodeContributor");

    public static final Resource R_WRITER = m.createResource(URI + "Writer");

    public static final Resource R_REPOSITORY = m.createResource(URI + "Repository");

    public static final Resource R_TECHNIQUE = m.createProperty(URI + "Technique");

    public static final Resource R_TOOL = m.createResource(URI + "Tool");

    public static final Property P_NAME = m.createProperty(URI, "name");

    public static final Property P_ABSTRACT = m.createProperty(URI, "abstract");

    public static final Property P_REPOSITORY = m.createProperty(URI, "repository");

    public static final Property P_CATEGORY = m.createProperty(URI, "category");

    public static final Property P_RELATED_PAPER = m.createProperty(URI, "relatedpaper");

    public static final Property P_AUTHOR = m.createProperty(URI, "author");

    public static final Property P_CONTRIBUTOR = m.createProperty(URI, "contributor");

    /*Since ontology v1.3.0*/
    public static final Property P_ORCID = m.createProperty(URI, "orcid");

    /*Since ontology v1.4.0*/

    public static final Property P_LAST_ACTIVITY_DATE = m.createProperty(URI, "lastActivityDate");

    public static final Property P_LAST_COMMIT_DATE = m.createProperty(URI, "lastCommitDate");

    public static final Property P_LAST_PUBLICATION_DATE = m.createProperty(URI, "lastPublicationDate");
}
