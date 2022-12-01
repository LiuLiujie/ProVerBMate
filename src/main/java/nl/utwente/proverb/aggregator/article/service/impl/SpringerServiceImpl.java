package nl.utwente.proverb.aggregator.article.service.impl;

import nl.utwente.proverb.aggregator.article.dto.article.ArticleDTO;
import nl.utwente.proverb.aggregator.exceptions.InvalidResourceURLException;
import nl.utwente.proverb.aggregator.article.service.ArticleService;
import nl.utwente.proverb.aggregator.article.dto.springer.SpringerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.Optional;

import static nl.utwente.proverb.aggregator.article.api.ArticleAPI.DOI_DOMAIN;
import static nl.utwente.proverb.aggregator.article.api.ArticleAPI.SPRINGER_API;

@Service("springer")
public class SpringerServiceImpl implements ArticleService {

    @Value("${springer-key}")
    private String key;

    @Resource
    private RestTemplateBuilder restTemplateBuilder;

    @Override
    public Optional<ArticleDTO> getArticle(String doi) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        var springerDTO = restTemplate.getForObject(getSpringerApi(doi), SpringerDTO.class);
        if (springerDTO == null || springerDTO.getRecords().isEmpty()){
            return Optional.empty();
        }
        return Optional.of(new ArticleDTO(springerDTO));
    }

    @Override
    public Optional<ArticleDTO> getArticleFromURL(String doiURL) throws InvalidResourceURLException {

        if (!doiURL.contains(DOI_DOMAIN)){
            throw new InvalidResourceURLException("Not DOI link");
        }
        String doi = doiURL.replace(DOI_DOMAIN, "");
        return this.getArticle(doi);
    }

    private String getSpringerApi(String doi){
        return SPRINGER_API + doi +"?api_key="+ key;
    }
}

