package nl.utwente.proverb.service.impl;

import nl.utwente.proverb.domain.dto.article.ArticleDTO;
import nl.utwente.proverb.domain.dto.crossref.CrossRefDTO;
import nl.utwente.proverb.exceptions.NoSuchArticleException;
import nl.utwente.proverb.service.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.Objects;

import static nl.utwente.proverb.domain.api.ArticleAPI.CROSSREF_API;
import static nl.utwente.proverb.domain.api.ArticleAPI.DOI_DOMAIN;

@Service("crossref")
public class CrossRefServiceImpl implements ArticleService {

    @Value("${cross-ref.mailto}")
    private String mailto;

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public ArticleDTO getArticle(String doi) throws NoSuchArticleException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        var crossRefDTO = Objects.requireNonNullElseGet(
                restTemplate.getForObject(getCrossRefAPI(doi), CrossRefDTO.class),
                ()-> {throw new NoSuchArticleException();});
        if (crossRefDTO.getMessage().getTitle() == null){
            throw new NoSuchArticleException();
        }
        return new ArticleDTO(crossRefDTO);
    }

    @Override
    public ArticleDTO getArticleFromURL(String doiURL) throws NoSuchArticleException {
        if (!doiURL.contains(DOI_DOMAIN)){
            throw new NoSuchArticleException("Not DOI link");
        }
        String doi = doiURL.replace(DOI_DOMAIN, "");
        return this.getArticle(doi);
    }

    private String getCrossRefAPI(String doi) {
        return CROSSREF_API + doi + "?mailto=" + mailto;
    }
}
