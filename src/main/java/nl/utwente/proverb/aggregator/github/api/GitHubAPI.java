package nl.utwente.proverb.aggregator.github.api;

public class GitHubAPI {

    private GitHubAPI(){ }

    public static final String GITHUB_DOMAIN = "github.com";

    public static final String GITHUB_API = "api."+GITHUB_DOMAIN;

    public static final String GITHUB_API_GET_REPO = GITHUB_API+"/repos";

    public static final String GITHUB_API_GET_ORG = GITHUB_API+"/orgs";
}
