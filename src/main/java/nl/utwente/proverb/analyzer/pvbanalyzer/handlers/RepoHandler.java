package nl.utwente.proverb.analyzer.pvbanalyzer.handlers;


import nl.utwente.proverb.analyzer.pvbanalyzer.entities.Repository;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDToolTemplate;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.util.DateUtil;
import nl.utwente.proverb.util.EscapeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RepoHandler implements BaseHandler {

    private final Resource tool;

    private final OntologyService ontologyService;

    private final List<String> mdRepositories;

    private final List<String> lastCommitDate;

    public RepoHandler(OntologyService ontologyService, MDTool mdTool) {
        this.ontologyService = ontologyService;
        this.tool = ontologyService.getToolResource(mdTool.getName());
        var urIs = mdTool.getProperty(MDToolTemplate.URIS);
        this.mdRepositories = extractMDGitHubRepository(urIs);
        this.lastCommitDate = mdTool.getProperty(MDToolTemplate.LAST_COMMIT_DATE);
    }

    @Override
    public void autoEnrichment() {
        this.updateMDLastCommitDate();
    }

    @Override
    public void reGeneration() {

    }

    public void updateMDLastCommitDate() {
        //Fetch MD date
        var repos = getRepoEntities(this.tool);
        if (repos.isEmpty()){
            return;
        }

        //Update last commit date for default branch
        var reposWithLastCommitDate = repos.stream()
                .filter(repo ->
                        repo.getLastCommitDate() !=null &&
                        !repo.getLastCommitDate().isEmpty() &&
                        DateUtil.formatDate(repo.getLastCommitDate()) != null)
                .collect(Collectors.toList());
        if (!reposWithLastCommitDate.isEmpty()) {
            this.lastCommitDate.clear();
            //Only one repo: update for default branch
            if (reposWithLastCommitDate.size() == 1){
                var date = DateUtil.formatDate(reposWithLastCommitDate.get(0).getLastCommitDate());
                if (date != null){
                    this.lastCommitDate.add(DateUtil.getDate(date) + " (default branch)");
                }
            }else{
                //Multi repos: multi dates
                for (var repo : reposWithLastCommitDate){
                    var date = DateUtil.formatDate(repo.getLastCommitDate());
                    if (date != null) {
                        this.lastCommitDate.add(repo.getName()+": "+DateUtil.getDate(date) + " (default branch)");
                    }
                }
            }
        }

        //Update last activity Date: only one for single tool
        var reposWithLastActivityDate = repos.stream()
                .filter(repo ->
                        !StringUtils.isBlank(repo.getLastActivityDate()) &&
                                DateUtil.formatDate(repo.getLastActivityDate()) != null)
                .collect(Collectors.toList());
        if (!reposWithLastActivityDate.isEmpty()){
            var latestActivityDate = DateUtil.formatDate(reposWithLastActivityDate.get(0).getLastActivityDate());
            if (reposWithLastCommitDate.size() > 1){
                for (var repo : reposWithLastCommitDate){
                    var curDate = DateUtil.formatDate(repo.getLastActivityDate());
                    if (curDate != null){
                        if (curDate.after(latestActivityDate)){
                            latestActivityDate = curDate;
                        }
                    }
                }
            }
            if (lastCommitDate != null){
                lastCommitDate.add(DateUtil.getDate(latestActivityDate) + " (last activity)");
            }
        }
    }

    private static List<String> extractMDGitHubRepository(List<String> urIs){

        if (urIs == null){
            return Collections.emptyList();
        }
        var repos = new ArrayList<String>();
        for (String repo : urIs){
            for (String str : repo.split(" ")){
                if (!str.contains("github.com")){
                    continue;
                }
                var url = EscapeUtil.escapeURL(str);
                if (url.contains("www.")){
                    url = url.replace("www.","");
                }
                if (url.contains("/blob")){
                    var pos = url.indexOf("/blob");
                    url = url.substring(0, pos);
                }
                if (url.contains("/wiki")){
                    var pos = url.indexOf("/wiki");
                    url = url.substring(0, pos);
                }
                repos.add(url);
            }
        }
        return repos;
    }

    private List<RDFNode> getRepositoryNodes(Resource tool) {
        return this.ontologyService.getProperties(tool, PROVERB.P_REPOSITORY);
    }

    public List<Repository> getRepoEntities(Resource tool) {

        var nodes = getRepositoryNodes(tool);
        var repos = new ArrayList<Repository>(nodes.size());
        for (var node : nodes){
            var dto = new Repository();
            this.ontologyService.getProperty((Resource) node, PROVERB.P_NAME).ifPresent(dto::setName);
            this.ontologyService.getProperty((Resource) node, PROVERB.P_ABSTRACT).ifPresent(dto::setAbs);
            this.ontologyService.getProperty((Resource) node, PROVERB.P_LAST_COMMIT_DATE).ifPresent(dto::setLastCommitDate);
            this.ontologyService.getProperty((Resource) node, PROVERB.P_LAST_ACTIVITY_DATE).ifPresent(dto::setLastActivityDate);
            repos.add(dto);
        }
        return repos;
    }
}
