package nl.utwente.proverb.aggregator.article.dto.crossref;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@NoArgsConstructor
public class Author {

    /**
     * ORCID: An URL to ORL webpage
     */
    @JsonAlias("ORCID")
    private @Nullable String orcid;

    private String given;

    private String family;

    private String sequence;
}