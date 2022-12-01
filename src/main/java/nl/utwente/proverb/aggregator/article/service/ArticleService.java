package nl.utwente.proverb.aggregator.article.service;

import nl.utwente.proverb.aggregator.article.dto.article.ArticleDTO;
import nl.utwente.proverb.aggregator.exceptions.InvalidResourceURLException;

import java.util.Optional;

public interface ArticleService {

    Optional<ArticleDTO> getArticle(String doi);

    Optional<ArticleDTO> getArticleFromURL(String doiURL) throws InvalidResourceURLException;
}
