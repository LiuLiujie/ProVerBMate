package nl.utwente.proverb.service;

import nl.utwente.proverb.domain.dto.article.ArticleDTO;
import nl.utwente.proverb.exceptions.NoSuchArticleException;

public interface ArticleService {

    ArticleDTO getArticle(String doi) throws NoSuchArticleException;

    ArticleDTO getArticleFromURL(String doiURL) throws NoSuchArticleException;
}
