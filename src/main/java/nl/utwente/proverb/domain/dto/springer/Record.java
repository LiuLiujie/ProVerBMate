package nl.utwente.proverb.domain.dto.springer;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class Record {

    private String title;

    @JsonAlias("creators")
    private List<Author> authors;

    private String publicationName;

    private String doi;

    private Date publicationDate;

    private Date printDate;

    @JsonAlias({"abstract"})
    private String abs;

    private List<String> keyword;
}