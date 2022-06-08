package nl.utwente.proverb.domain.dto.crossref;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Author {

    @JsonAlias("ORCID")
    private String orcid;

    private String given;

    private String family;

    private String sequence;
}