package nl.utwente.proverb.aggregator.article.dto.article;

import lombok.Getter;
import org.springframework.lang.Nullable;


public class Author {

    @Getter
    private String given;

    @Getter
    private String family;

    private String name;

    @Getter
    private @Nullable String email;

    @Getter
    private @Nullable String orcid;

    public Author(nl.utwente.proverb.aggregator.article.dto.crossref.Author author){
        this.given = author.getGiven();
        this.family = author.getFamily();
        this.orcid = author.getOrcid();
    }

    public Author(nl.utwente.proverb.aggregator.article.dto.springer.Author author){
        String[] str = author.getName().split(", ");
        if (str.length == 2){
            this.family = str[0];
            this.given = str[1];
        } else {
           this.name = author.getName();
        }
    }

    public String getName() {
        if (given != null && family != null){
            return given + " "+ family;
        } else {
            return name;
        }
    }
}
