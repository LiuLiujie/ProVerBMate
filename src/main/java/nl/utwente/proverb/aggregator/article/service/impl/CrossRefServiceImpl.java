package nl.utwente.proverb.aggregator.article.service.impl;

import nl.utwente.proverb.aggregator.article.service.ArticleService;
import nl.utwente.proverb.aggregator.article.dto.article.ArticleDTO;
import nl.utwente.proverb.aggregator.article.dto.crossref.CrossRefDTO;
import nl.utwente.proverb.aggregator.exceptions.InvalidResourceURLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.Optional;

import static nl.utwente.proverb.aggregator.article.api.ArticleAPI.CROSSREF_API;
import static nl.utwente.proverb.aggregator.article.api.ArticleAPI.DOI_DOMAIN;

@Service("crossref")
public class CrossRefServiceImpl implements ArticleService {

    @Value("${cross-ref.mailto}")
    private String mailto;

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Optional<ArticleDTO> getArticle(String doi) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        try {
            var crossRefDTO = restTemplate.getForObject(getCrossRefAPI(doi), CrossRefDTO.class);
            if (crossRefDTO == null || crossRefDTO.getMessage().getTitle() == null){
                return Optional.empty();
            }
            return Optional.of(new ArticleDTO(crossRefDTO));
        }catch (RestClientException e){
            return Optional.empty();
        }
    }

    @Override
    public Optional<ArticleDTO> getArticleFromURL(String doiURL) throws InvalidResourceURLException {
        if (!doiURL.contains(DOI_DOMAIN)){
            throw new InvalidResourceURLException("Not DOI link");
        }
        String doi = doiURL.replace(DOI_DOMAIN, "");
        return this.getArticle(doi);
    }

    private String getCrossRefAPI(String doi) {
        return CROSSREF_API + doi + "?mailto=" + mailto;
    }
}
