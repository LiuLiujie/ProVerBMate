package nl.utwente.proverb.aggregator.github.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class GitHubCommitterDTO {

    private String name;

    private String email;

    private Date date;
}
