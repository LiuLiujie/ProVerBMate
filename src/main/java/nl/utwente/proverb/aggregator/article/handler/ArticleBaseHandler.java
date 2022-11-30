package nl.utwente.proverb.aggregator.article.handler;

import nl.utwente.proverb.util.EscapeUtil;
import org.apache.jena.rdf.model.Resource;

public class ArticleBaseHandler extends ArticleHandler{

    @Override
    public boolean handle(String url, Resource articleResource) {
        if (isCorrespondingURL(url)){
            var escapeURL = EscapeUtil.escapeURL(url);
            return handleNext(escapeURL, articleResource);
        }
        return false;
    }
}
