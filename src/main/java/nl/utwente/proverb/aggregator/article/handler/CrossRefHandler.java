package nl.utwente.proverb.aggregator.article.handler;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.article.dto.article.Author;
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
        try {
            var dto = dtoOpt.get();
            ontologyService.addUniqueProperty(articleResource, PROVERB.P_NAME, dto.getTitle());
            ontologyService.addUniqueProperty(articleResource, PROVERB.P_ABSTRACT, dto.getAbs());
            for (var author : dto.getAuthors()){
                this.handleAuthor(author, articleResource);
            }
            log.info("Article Crossref handler success");
            return true;
        }catch (Exception e){
            log.info("Article Crossref handler fail: {}", url);
            log.error(e.getStackTrace());
            return false;
        }
    }

    public void handleAuthor(Author author, Resource articleResource){
        try {
            var writer = ontologyService.createWriter(articleResource, author.getName());
            ontologyService.addUniqueProperty(writer, PROVERB.P_NAME, author.getName());
            if (author.getOrcid() != null){
                //Only some authors have orcid.
                ontologyService.addUniqueProperty(writer, PROVERB.P_ORCID, author.getOrcid());
            }
        }catch (Exception e){
            log.info("Article Crossref handler author fail");
            log.error(e.getStackTrace());
        }
    }
}
