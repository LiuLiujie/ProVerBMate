package nl.utwente.proverb.domain.dto.evaluate;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class EvaluatePatternTemplate {

    private String name;

    private String description;

    private double version;

    private String domain;

    private String range;

    private String relationship;

    private List<Pattern> patterns;

    @JsonIgnore
    private int issueNumber;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class Pattern{
        @JsonAlias("domain_property")
        String domainProperty;

        @JsonAlias("range_property")
        String rangeProperty;

        List<String> methods;
    }
}
