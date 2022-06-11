package nl.utwente.proverb.service.impl;

import nl.utwente.proverb.domain.dto.article.ArticleDTO;
import nl.utwente.proverb.domain.dto.crossref.CrossRefDTO;
import nl.utwente.proverb.exceptions.InvalidResourceURLException;
import nl.utwente.proverb.service.ArticleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import java.util.Optional;

import static nl.utwente.proverb.domain.api.ArticleAPI.CROSSREF_API;
import static nl.utwente.proverb.domain.api.ArticleAPI.DOI_DOMAIN;

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
            return Optional.of(new ArticleDTO(new CrossRefDTO()));
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
