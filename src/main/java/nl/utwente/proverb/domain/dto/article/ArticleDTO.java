package nl.utwente.proverb.domain.dto.article;

import lombok.Getter;
import nl.utwente.proverb.domain.dto.crossref.CrossRefDTO;
import nl.utwente.proverb.domain.dto.springer.SpringerDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArticleDTO {

    String title;

    String abs;

    String doi;

    List<Author> authors;

    public ArticleDTO(CrossRefDTO dto) {
        var article = dto.getMessage();
        this.title = article.getTitle().get(0);
        this.doi = article.getDoi();
        if (article.getAuthor() != null){
            this.authors = new ArrayList<>(article.getAuthor().size());
            article.getAuthor().forEach(author -> this.authors.add(new Author(author)));
        }
    }

    public ArticleDTO(SpringerDTO dto) {
        var article = dto.getRecords().get(0);
        this.title = article.getTitle();
        this.abs = article.getAbs();
        this.doi = article.getDoi();
        this.authors = new ArrayList<>(article.getAuthors().size());
        article.getAuthors().forEach(creator -> this.authors.add(new Author(creator)));
    }
}
