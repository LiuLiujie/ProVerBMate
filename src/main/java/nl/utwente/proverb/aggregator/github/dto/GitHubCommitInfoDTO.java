package nl.utwente.proverb.aggregator.github.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GitHubCommitInfoDTO {

    private GitHubCommitterDTO author;

    private GitHubCommitterDTO committer;

    private String message;
}
