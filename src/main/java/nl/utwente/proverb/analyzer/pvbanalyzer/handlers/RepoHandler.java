package nl.utwente.proverb.analyzer.pvbanalyzer.handlers;


import nl.utwente.proverb.analyzer.pvbanalyzer.entities.Repository;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDToolTemplate;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.ontology.service.OntologyService;
import nl.utwente.proverb.util.DateUtil;
import nl.utwente.proverb.util.EscapeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RepoHandler extends BaseHandler {

    private final Resource toolResource;

    private final MDTool mdTool;

    private final OntologyService ontologyService;

    public RepoHandler(OntologyService ontologyService, MDTool mdTool) {
        this.ontologyService = ontologyService;
        this.mdTool = mdTool;
        this.toolResource = ontologyService.getToolResource(mdTool.getName());
    }

    @Override
    public void enrichment() {
        List<String> lastCommitDate = this.mdTool.getProperty(MDToolTemplate.LAST_COMMIT_DATE);
        this.updateMDLastCommitDate(lastCommitDate);
    }

    @Override
    public void extractInfo() {
        var urIs = mdTool.getProperty(MDToolTemplate.URIS);
        List<String> mdRepositories = this.extractMDGitHubRepository(urIs);
        for (var repoURL : mdRepositories){
            var repoResource = this.ontologyService.createRepository(repoURL);
            this.ontologyService.addProperty(this.toolResource, PROVERB.P_REPOSITORY, repoResource);
        }
    }

    private void updateMDLastCommitDate(List<String> lastCommitDate) {
        //Fetch MD date
        var repos = getRepoEntities(this.toolResource);
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
            lastCommitDate.clear();
            //Only one repo: update for default branch
            if (reposWithLastCommitDate.size() == 1){
                var date = DateUtil.formatDate(reposWithLastCommitDate.get(0).getLastCommitDate());
                if (date != null){
                    lastCommitDate.add(DateUtil.getDate(date) + " (default branch)");
                }
            }else{
                //Multi repos: multi dates
                for (var repo : reposWithLastCommitDate){
                    var date = DateUtil.formatDate(repo.getLastCommitDate());
                    if (date != null) {
                        if (repo.getOwner()!=null){
                            lastCommitDate.add(repo.getOwner().getUsername()+"/"+repo.getName()+": "+DateUtil.getDate(date) + " (default branch)");
                        }else{
                            lastCommitDate.add(repo.getName()+": "+DateUtil.getDate(date) + " (default branch)");
                        }
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
            if (reposWithLastCommitDate.isEmpty()){
                //clear the old data if it is not cleaned before
                lastCommitDate.clear();
            }
            var latestActivityDate = DateUtil.formatDate(reposWithLastActivityDate.get(0).getLastActivityDate());
            if (reposWithLastCommitDate.size() > 1){
                for (var repo : reposWithLastCommitDate){
                    var curDate = DateUtil.formatDate(repo.getLastActivityDate());
                    if (curDate != null && curDate.after(latestActivityDate)){
                        latestActivityDate = curDate;
                    }
                }
            }
            lastCommitDate.add(DateUtil.getDate(latestActivityDate) + " (last activity)");
        }
    }

    private List<String> extractMDGitHubRepository(List<String> urIs){

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

    private List<Repository> getRepoEntities(Resource tool) {

        var repoNodes = this.ontologyService.getProperties(tool, PROVERB.P_REPOSITORY);
        var repos = new ArrayList<Repository>(repoNodes.size());
        for (var repoNode : repoNodes){
            var dto = new Repository();
            this.ontologyService.getProperty((Resource) repoNode, PROVERB.P_NAME)
                    .ifPresent(dto::setName);
            this.ontologyService.getProperty((Resource) repoNode, PROVERB.P_ABSTRACT)
                    .ifPresent(dto::setAbs);
            this.ontologyService.getProperty((Resource) repoNode, PROVERB.P_LAST_COMMIT_DATE)
                    .ifPresent(dto::setLastCommitDate);
            this.ontologyService.getProperty((Resource) repoNode, PROVERB.P_LAST_ACTIVITY_DATE)
                    .ifPresent(dto::setLastActivityDate);
            var ownerNodes = this.ontologyService.getProperties((Resource) repoNode, PROVERB.P_REPO_OWNER);
            if (!ownerNodes.isEmpty()){
                var ownerNodeOpt = ownerNodes.stream().findFirst();
                if (ownerNodeOpt.isPresent()){
                    var ownerNode = ownerNodeOpt.get();
                    var owner = new Repository.Contributor();
                    this.ontologyService.getProperty((Resource) ownerNode, PROVERB.P_USERNAME).ifPresent(owner::setUsername);
                    dto.setOwner(owner);
                }
            }
            repos.add(dto);
        }
        return repos;
    }
}
