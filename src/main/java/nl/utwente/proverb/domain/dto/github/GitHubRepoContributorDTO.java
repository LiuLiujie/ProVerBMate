package nl.utwente.proverb.domain.dto.github;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GitHubRepoContributorDTO {

    /*
        Contributor
     */
    @JsonAlias({"login"})
    private String username;

    @JsonAlias({"url"})
    private String homeRestURL;

    @JsonAlias({"html_url"})
    private String homeHTMLURL;

    @JsonAlias({"contributions"})
    private int totalCommits;

    private GitHubUserDTO detail;
}
