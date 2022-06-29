package nl.utwente.proverb.domain.dto.github;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GitHubRepoIssueDTO {

    String title;

    String body;

    List<Label> labels;

    int number;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Label{
        String name;
    }
}
