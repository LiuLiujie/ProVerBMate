package nl.utwente.proverb.domain.dto.github;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GitHubRepoDTO {

    @JsonAlias("name")
    private String repoName;

    @JsonAlias("description")
    private String abs;

    @JsonAlias({"contributors_url"})
    private String contributorsRestURL;

    @JsonAlias({"pushed_at"})
    private Date latestPush;

    private String repoRestURL;

    private List<GitHubRepoContributorDTO> contributors;
}
