package nl.utwente.proverb.aggregator.github.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GitHubRepoDTO {

    @JsonAlias("name")
    private String repoName;

    @JsonAlias("owner")
    private GitHubRepoContributorDTO owner;

    @JsonAlias("description")
    private String abs;

    @JsonAlias({"contributors_url"})
    private String contributorsRestURL;

    @JsonAlias({"pushed_at"})
    private Date lastCommit;

    @JsonAlias({"default_branch"})
    private String defaultBranch;

    private String repoRestURL;
}
