package nl.utwente.proverb.aggregator.github.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GitHubCommitDTO {

    private String sha;

    private String url;

    @JsonAlias("commit")
    private GitHubCommitInfoDTO commitInfo;
}
