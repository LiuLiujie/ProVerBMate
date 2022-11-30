package nl.utwente.proverb.aggregator.github.handlers;

import nl.utwente.proverb.aggregator.github.api.GitHubAPI;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Service;

@Service
public abstract class GitHubHandler {

    private GitHubHandler nextHandler;

    public static GitHubHandler setHandlerChain(GitHubHandler... handlers) {
        GitHubHandler head = new GitHubBaseHandler();
        GitHubHandler first = head;
        for (GitHubHandler next : handlers){
            head.nextHandler = next;
            head = next;
        }
        return first;
    }

    public abstract boolean handle(String url, Resource githubResource);

    protected boolean handleNext(String url, Resource githubResource){
        if (nextHandler != null){
            return nextHandler.handle(url , githubResource);
        }
        return false;
    }

    /**
     * Check if the url is GitHub url
     * @param url   any url
     * @return      true when it is GitHub URL
     */
    protected abstract boolean isCorrespondingURL(String url);

    protected static String removeGitHubPrefix(String githubURL){
        return githubURL.replace("https://"+ GitHubAPI.GITHUB_DOMAIN+"/", "");
    }
}
