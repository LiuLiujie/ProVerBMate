package nl.utwente.proverb.service.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.domain.dto.evaluate.EvaluatePatternTemplate;
import nl.utwente.proverb.service.EvaluateService;
import nl.utwente.proverb.service.GitHubService;
import nl.utwente.proverb.service.OntologyService;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class EvaluateServiceImpl implements EvaluateService {

    private static final String REPO_ISSUES_API = "https://api.github.com/repos/LiuLiujie/ProVerBMate/issues";

    @Resource
    private GitHubService githubService;

    @Resource
    private OntologyService ontologyService;

    @Override
    public List<EvaluatePatternTemplate> getTemplatesFromGitHub() {
        var issues = githubService.getGitHubRepoIssues(REPO_ISSUES_API, "open", "automate");
        var templates = new ArrayList<EvaluatePatternTemplate>(issues.size());
        for (var issue : issues){
            try {
                var template = new ObjectMapper().readValue(issue.getBody(), EvaluatePatternTemplate.class);
                template.setIssueNumber(issue.getNumber());
                templates.add(template);
            }catch (JacksonException e){
                log.error("invalid issue: number={}", issue.getNumber());
            }
        }
        return templates;
    }

    @Override
    public int handleSameAsRelationship(EvaluatePatternTemplate template) {
        var query = this.getQueryFromTemplate(template);
        var results = ontologyService.executeSPARQLQuery(query);
        var domains = results.getValues().get("domain");
        var ranges = results.getValues().get("range");
        if (domains.size() != ranges.size()){
            log.error("Invalid result");
            return 0;
        }
        for (int i=0; i< domains.size(); i++){
            ontologyService.addSameAs((org.apache.jena.rdf.model.Resource) domains.get(i),(org.apache.jena.rdf.model.Resource) ranges.get(i));
        }
        return domains.size();
    }

    private Query getQueryFromTemplate(EvaluatePatternTemplate template) {
        ParameterizedSparqlString queryStr = new ParameterizedSparqlString();
        queryStr.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        queryStr.setNsPrefix("pvb", "http://slebok.github.io/proverb/ontology#");
        queryStr.append("SELECT ?domain ?range"+"\n");
        queryStr.append("WHERE"+"\n");
        queryStr.append("{"+"\n");
        queryStr.append("?domain rdf:type pvb:" + template.getDomain() + "."+"\n");
        queryStr.append("?range rdf:type pvb:" + template.getRange() + "."+"\n");
        var patterns = template.getPatterns();
        for (int i=0; i< patterns.size(); i++){
            var pattern = patterns.get(i);
            queryStr.append("?domain pvb:"+pattern.getDomainProperty() + " ?dom_pro"+i+" ."+"\n");
            queryStr.append("?range pvb:"+pattern.getRangeProperty() + " ?ran_pro"+i+" ."+"\n");
            for (var method: pattern.getMethods()){
                switch (method){
                    case "approximate equal":
                        queryStr.append("FILTER regex(?dom_pro"+i+", ?ran_pro"+i+", \"i\") ."+"\n");
                        break;
                    case "equal":
                        queryStr.append("FILTER (?dom_pro"+i+"=?ran_pro"+i+") ."+"\n");
                        break;
                    default:
                        log.error("Not support method: {}", method);
                }
            }
        }
        queryStr.append("}");
        return queryStr.asQuery();
    }

    public EvaluatePatternTemplate getTemplatesFromString(String string){
        try {
            return new ObjectMapper().readValue(string, EvaluatePatternTemplate.class);
        }catch (JacksonException e){
            log.error("invalid json string");
        }
        return null;
    }
}
