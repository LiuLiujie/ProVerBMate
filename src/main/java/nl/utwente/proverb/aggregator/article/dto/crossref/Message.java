package nl.utwente.proverb.aggregator.article.dto.crossref;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Message {

    private String publisher;

    @JsonAlias("DOI")
    private String doi;

    private String type;

    private Created created;

    private List<String> title;

    @Nullable
    private List<Author> author;

    private List<String> subject;
}