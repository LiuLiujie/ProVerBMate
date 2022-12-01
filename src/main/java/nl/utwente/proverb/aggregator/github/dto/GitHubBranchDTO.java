package nl.utwente.proverb.aggregator.github.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GitHubBranchDTO {

    private String name;

    private GitHubCommitDTO commit;
}
