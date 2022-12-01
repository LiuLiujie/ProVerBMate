package nl.utwente.proverb.aggregator.article.handler;

import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class ArticleHandler {

    private ArticleHandler nextHandler;

    public static ArticleHandler setHandlerChain(ArticleHandler... handlers) {
        ArticleHandler head = new ArticleBaseHandler();
        ArticleHandler first = head;
        for (ArticleHandler next : handlers){
            head.nextHandler = next;
            head = next;
        }
        return first;
    }

    public abstract boolean handle(String url, Resource articleResource);

    protected boolean handleNext(String url, Resource articleResource){
        if (nextHandler != null){
            return nextHandler.handle(url , articleResource);
        }
        return false;
    }

    /**
     * Check if the url is GitHub url
     * @param url   any url
     * @return      true when it is GitHub URL
     */
    protected boolean isCorrespondingURL(String url){
        return url.contains("doi.org");
    }
}
