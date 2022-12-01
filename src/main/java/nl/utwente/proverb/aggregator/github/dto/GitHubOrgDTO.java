package nl.utwente.proverb.aggregator.github.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GitHubOrgDTO {

    @JsonAlias("name")
    private String name;

    private String description;

    @JsonAlias("updated_at")
    private Date lastActivity;
}
