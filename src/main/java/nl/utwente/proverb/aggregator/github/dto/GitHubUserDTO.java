package nl.utwente.proverb.aggregator.github.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GitHubUserDTO {

    @JsonAlias({"name"})
    private String name;

    @JsonAlias({"company"})
    private String company;

    @JsonAlias({"bio"})
    private String abs;
}
