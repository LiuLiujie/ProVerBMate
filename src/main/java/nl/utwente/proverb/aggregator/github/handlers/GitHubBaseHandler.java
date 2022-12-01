package nl.utwente.proverb.aggregator.github.handlers;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.aggregator.github.api.GitHubAPI;
import nl.utwente.proverb.util.EscapeUtil;
import org.apache.jena.rdf.model.Resource;


@Log4j2
public class GitHubBaseHandler extends GitHubHandler{

    public boolean handle(String url, Resource githubResource) {
        if (isCorrespondingURL(url)){
            var escapeURL = EscapeUtil.escapeURL(url);
            return handleNext(escapeURL, githubResource);
        }
        return false;
    }

    @Override
    protected boolean isCorrespondingURL(String url) {
        return url.contains(GitHubAPI.GITHUB_DOMAIN);
    }
}
