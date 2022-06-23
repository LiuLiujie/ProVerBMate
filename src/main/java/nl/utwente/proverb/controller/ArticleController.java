package nl.utwente.proverb.controller;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.domain.dto.article.ArticleDTO;
import nl.utwente.proverb.domain.ontology.PROVERB;
import nl.utwente.proverb.service.ArticleService;
import nl.utwente.proverb.service.OntologyService;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Log4j2
@Controller
public class ArticleController {

    @javax.annotation.Resource
    Map<String, ArticleService> articleServices;

    @javax.annotation.Resource
    private OntologyService ontologyService;

    public boolean enrichArticle(Resource articleResource) {
        var url = articleResource.toString();
        log.info("Enriching article: {}", url);
        var springer = articleServices.get("springer");
        var opt = springer.getArticleFromURL(url);
        //Here can be refactored using both APIs to get more like orcid and to make cross validation
        boolean success = false;
        if (opt.isPresent()){
            success = enrichUsingSpringer(articleResource, opt.get());
        }
        if (success){
            log.info("Successfully enrich article using springer");
            return true;
        }
        log.info("Springer doesn't have: {}. Using alternative source for article", url);
        var corssref = articleServices.get("crossref");
        opt = corssref.getArticleFromURL(url);
        if (opt.isPresent()) {
            success = enrichUsingCrossRef(articleResource, opt.get());
            if (success){
                log.info("Successfully enrich article using crossref");
                return true;
            }
        }
        log.error("Enriching article: {} fail", url);
        return false;
    }

    private boolean enrichUsingSpringer(Resource articleResource, ArticleDTO dto){
        try {
            ontologyService.addProperty(articleResource, PROVERB.P_NAME, dto.getTitle());
            ontologyService.addProperty(articleResource, PROVERB.P_ABSTRACT, dto.getAbs());
            for (var author : dto.getAuthors()){
                var writer = ontologyService.createWriter(articleResource, author.getName());
                ontologyService.addProperty(writer, PROVERB.P_NAME, author.getName());
                //TODO: Get email
                //https://dev.elsevier.com/documentation/AuthorRetrievalAPI.wadl
                //
            }
            return true;
        }catch (RuntimeException e){
            return false;
        }
    }

    private boolean enrichUsingCrossRef(Resource articleResource, ArticleDTO dto){
        try {
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
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }
}
