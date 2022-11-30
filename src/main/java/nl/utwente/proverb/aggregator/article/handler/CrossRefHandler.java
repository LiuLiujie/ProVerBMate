package nl.utwente.proverb.aggregator.article.handler;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.article.service.ArticleService;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.ontology.PROVERB;
import org.apache.jena.rdf.model.Resource;

@Log4j2
public class CrossRefHandler extends ArticleHandler{

    private final ArticleService crossrefService;

    private final OntologyService ontologyService;

    public CrossRefHandler(ArticleService crossrefService, OntologyService ontologyService) {
        this.crossrefService = crossrefService;
        this.ontologyService = ontologyService;
    }

    @Override
    public boolean handle(String url, Resource articleResource) {
        log.info("Article Crossref handler start: {}", url);
        var dtoOpt = crossrefService.getArticleFromURL(url);
        if (dtoOpt.isEmpty()){
            log.info("Article Crossref handler fail: {}", url);
            return handleNext(url, articleResource);
        }
        var dto = dtoOpt.get();
        ontologyService.addProperty(articleResource, PROVERB.P_NAME, dto.getTitle());
        ontologyService.addProperty(articleResource, PROVERB.P_ABSTRACT, dto.getAbs());
        for (var author : dto.getAuthors()){
            var writer = ontologyService.createWriter(articleResource, author.getName());
            ontologyService.addProperty(writer, PROVERB.P_NAME, author.getName());
            if (author.getOrcid() != null){
                //Only some authors have orcid.
                ontologyService.addProperty(writer, PROVERB.P_ORCID, author.getOrcid());
            }
        }
        log.info("Article Crossref handler success");
        return true;
    }
}