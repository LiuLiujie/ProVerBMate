package nl.utwente.proverb.service;

import nl.utwente.proverb.domain.dto.article.ArticleDTO;
import nl.utwente.proverb.exceptions.InvalidResourceURLException;

import java.util.Optional;

public interface ArticleService {

    Optional<ArticleDTO> getArticle(String doi);

    Optional<ArticleDTO> getArticleFromURL(String doiURL) throws InvalidResourceURLException;
}
