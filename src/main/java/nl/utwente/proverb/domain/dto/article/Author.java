package nl.utwente.proverb.domain.dto.article;

import lombok.Getter;
import lombok.Setter;


public class Author {

    @Setter
    @Getter
    private String given;

    @Setter
    @Getter
    private String family;

    private String name;

    @Setter
    @Getter
    private String contact;

    public Author(nl.utwente.proverb.domain.dto.crossref.Author author){
        this.given = author.getGiven();
        this.family = author.getFamily();
        this.contact = author.getOrcid();
    }

    public Author(nl.utwente.proverb.domain.dto.springer.Author author){
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
            return family + " " +given;
        } else {
            return name;
        }
    }
}
