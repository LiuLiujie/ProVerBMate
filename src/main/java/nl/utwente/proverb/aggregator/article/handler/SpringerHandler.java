package nl.utwente.proverb.aggregator.article.handler;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.article.service.ArticleService;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.ontology.PROVERB;
import org.apache.jena.rdf.model.Resource;

@Log4j2
public class SpringerHandler extends ArticleHandler{

    private final ArticleService springerService;

    private final OntologyService ontologyService;

    public SpringerHandler(ArticleService springerService, OntologyService ontologyService) {
        this.springerService = springerService;
        this.ontologyService = ontologyService;
    }

    @Override
    public boolean handle(String url, Resource articleResource) {
        log.info("Article Springer handler start: {}", url);
        var optDto = springerService.getArticleFromURL(url);
        if (optDto.isEmpty()){
            log.info("Article Springer handler fail: {}", url);
            return handleNext(url, articleResource);
        }
        var dto = optDto.get();
        ontologyService.addProperty(articleResource, PROVERB.P_NAME, dto.getTitle());
        ontologyService.addProperty(articleResource, PROVERB.P_ABSTRACT, dto.getAbs());
        for (var author : dto.getAuthors()){
            var writer = ontologyService.createWriter(articleResource, author.getName());
            ontologyService.addProperty(writer, PROVERB.P_NAME, author.getName());
            //TODO: Get email
            //https://dev.elsevier.com/documentation/AuthorRetrievalAPI.wadl
        }
        log.info("Article Springer handler success");
        return true;
    }
}
