package nl.utwente.proverb.analyzer.pvbanalyzer.handlers;

import nl.utwente.proverb.analyzer.pvbanalyzer.entities.Paper;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDToolTemplate;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.util.EscapeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.*;

public class PaperHandler extends BaseHandler {

    private final Resource toolResource;

    private final OntologyService ontologyService;

    private final MDTool mdTool;

    public PaperHandler(OntologyService ontologyService, MDTool mdTool){
        this.ontologyService = ontologyService;
        this.mdTool = mdTool;
        this.toolResource = this.ontologyService.getToolResource(mdTool.getName());
    }

    @Override
    public void enrichment() {
        var mdPapers = mdTool.getProperty(MDToolTemplate.PAPERS);
        this.enrichPaper(mdPapers);
    }

    @Override
    public void extractInfo() {
        var mdPapers = mdTool.getProperty(MDToolTemplate.PAPERS);
        var papers = this.extractPapers(mdPapers);
        for (var paper : papers){
            if (!StringUtils.isBlank(paper.getDoiURL())){
                var articleResource = ontologyService.createArticle(paper.getDoiURL());
                if (!StringUtils.isBlank(paper.getTitle())){
                    ontologyService.addUniqueProperty(articleResource, PROVERB.P_NAME, paper.getTitle());
                }
            }
        }
    }

    public void enrichPaper(List<String> mdPapers){
        var entities = getPaperEntities(this.toolResource);
        for (int i=0; i< mdPapers.size(); i++){
            var mdPaper = mdPapers.get(i);
            if (containsTitle(mdPaper)){
                //If already have the title, no need to enrich
                continue;
            }
            var oldPaper = extractPaper(mdPaper);
            var paperEntity = entities.stream().filter(entity -> entity.getDoiURL().equals(oldPaper.getDoiURL())).findFirst();
            if (paperEntity.isPresent()){
                var enrichedPaper = enrichTitle(mdPaper, paperEntity.get().getTitle(), paperEntity.get().getDoiURL());
                mdPapers.set(i, enrichedPaper);
            }
        }
    }

    private List<Paper> extractPapers(List<String> mdPapers){
        var papers = new ArrayList<Paper>(mdPapers.size());
        for (String mdPaper: mdPapers){
            var paper = extractPaper(mdPaper);
            papers.add(paper);
        }
        return papers;
    }

    private Paper extractPaper(String mdPaper){
        var paper = new Paper();
        if (containsTitle(mdPaper)){
            //With title already
            String title = mdPaper.substring(mdPaper.indexOf("["), mdPaper.indexOf("]"));
            String doi = mdPaper.substring(mdPaper.indexOf('('), mdPaper.indexOf(')'));
            if (doi.contains("doi.org")){
                paper.setTitle(title);
            }
        }else{
            //Without title
            for (String str : mdPaper.split(" ")){
                if (str.contains("doi.org")){
                    paper.setDoiURL(EscapeUtil.escapeURL(str));
                }
            }
        }
        return paper;
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
        return str.contains("[") && str.contains("]");
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
