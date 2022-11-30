package nl.utwente.proverb.aggregator.article;

import nl.utwente.proverb.aggregator.article.handler.ArticleHandler;
import nl.utwente.proverb.aggregator.article.handler.CrossRefHandler;
import nl.utwente.proverb.aggregator.article.handler.SpringerHandler;
import nl.utwente.proverb.aggregator.article.service.ArticleService;
import nl.utwente.proverb.ontology.service.OntologyService;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ArticleAggregator {

    @javax.annotation.Resource
    Map<String, ArticleService> articleServices;

    @javax.annotation.Resource
    private OntologyService ontologyService;

    public boolean aggregateDOIURL(String url, Resource articleResource){
        var handler = ArticleHandler.setHandlerChain(
                new SpringerHandler(articleServices.get("springer"), this.ontologyService),
                new CrossRefHandler(articleServices.get("crossref"), this.ontologyService)
        );
        return handler.handle(url, articleResource);
    }
}
