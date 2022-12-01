package nl.utwente.proverb.analyzer.pvbanalyzer.handlers;

import nl.utwente.proverb.analyzer.pvbanalyzer.entities.Paper;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDToolTemplate;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.util.EscapeUtil;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaperHandler implements BaseHandler {

    private final Resource tool;

    private final OntologyService ontologyService;

    private final List<String> mdPapers;

    public PaperHandler(OntologyService ontologyService, MDTool mdTool){
        this.ontologyService = ontologyService;
        this.tool = this.ontologyService.getToolResource(mdTool.getName());
        this.mdPapers = mdTool.getProperty(MDToolTemplate.PAPERS);
    }

    @Override
    public void autoEnrichment() {
        this.enrichPaper();
    }

    public void enrichPaper(){
        var entities = getPaperEntities(this.tool);
        for (int i = 0; i< mdPapers.size(); i++) {
            var mdPaper = mdPapers.get(i);
            var doi = this.extractMDDoi(mdPaper);
            if (doi.isEmpty()){
                continue;
            }
            var paperEntity = entities.stream().filter(entity -> entity.getDoiURL().equals(doi.get())).findFirst();
            if (paperEntity.isPresent()){
                var enrichedPaper = enrichTitle(mdPaper, paperEntity.get().getTitle(), paperEntity.get().getDoiURL());
                mdPapers.set(i, enrichedPaper);
            }
        }
    }

    @Override
    public void reGeneration() {
        throw new UnsupportedOperationException();
    }

    private Optional<String> extractMDDoi(String mdPaper){
        for (String str : mdPaper.split(" ")){
            if (str.contains("doi.org")){
                return Optional.of(EscapeUtil.escapeURL(str));
            }
        }
        return Optional.empty();
    }

    private String enrichTitle(String origin, String title, String doi) {
        if (containsTitle(origin)){
            return origin;
        }
        String newTitle = "["+title+"]"+"("+doi+")";
        return origin.replace(doi, newTitle);
    }

    private List<RDFNode> getPaperNodes(Resource tool) {
        return this.ontologyService.getProperties(tool, PROVERB.P_RELATED_PAPER);
    }

    public List<Paper> getPaperEntities(Resource tool) {
        var nodes = getPaperNodes(tool);
        var articles = new ArrayList<Paper>(nodes.size());
        for (var node : nodes){
            var dto = new Paper();
            dto.setDoiURL(node.toString());
            this.ontologyService.getProperty((Resource) node, PROVERB.P_NAME).ifPresent(dto::setTitle);
            articles.add(dto);
        }
        return articles;
    }

    private static boolean containsTitle(String str){
        if (str.startsWith("http")){
            return false;
        }
        return str.startsWith("[") && str.contains("]");
    }

    private static boolean containsConference(String str){
        if (!str.contains("'")){
            return false;
        }

        var sp = str.split("'");
        if (sp.length != 2){
            return false;
        }

        try {
            Integer.parseInt(sp[1]);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
