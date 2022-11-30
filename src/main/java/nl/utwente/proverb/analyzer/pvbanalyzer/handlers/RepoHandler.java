package nl.utwente.proverb.analyzer.pvbanalyzer.handlers;


import nl.utwente.proverb.analyzer.pvbanalyzer.entities.Repository;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDToolTemplate;
import nl.utwente.proverb.ontology.PROVERB;
import nl.utwente.proverb.ontology.PVBModel;
import nl.utwente.proverb.util.DateUtil;
import nl.utwente.proverb.util.EscapeUtil;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RepoHandler implements BaseHandler {

    private final Resource tool;

    private final PVBModel model;

    private final List<String> mdRepositories;

    private final List<String> lastCommitDate;

    public RepoHandler(PVBModel model, MDTool mdTool) {
        this.model = model;
        this.tool = model.getTool(mdTool.getName());
        var urIs = mdTool.getProperty(MDToolTemplate.URIS);
        this.mdRepositories = extractMDGitHubRepository(urIs);
        this.lastCommitDate = mdTool.getProperty(MDToolTemplate.LAST_COMMIT_DATE);
    }

    @Override
    public void autoEnrichment() {
        this.updateLastCommitDate();
    }

    @Override
    public void reGeneration() {

    }

    public void updateLastCommitDate() {
        var repos = getPaperEntities(this.model, this.tool);
        if (repos.isEmpty()){
            return;
        }
        var reposWithLastCommitDate = repos.stream()
                .filter(repo -> repo.getLastCommitDate()!=null
                        && !repo.getLastCommitDate().isEmpty()
                        && DateUtil.formatDate(repo.getLastCommitDate()) != null)
                .collect(Collectors.toList());
        if (reposWithLastCommitDate.isEmpty()){
            return;
        }
        var latest = DateUtil.formatDate(reposWithLastCommitDate.get(0).getLastCommitDate());
        for (var repo : reposWithLastCommitDate){
            var curDate = DateUtil.formatDate(repo.getLastCommitDate());
            assert curDate != null;
            if (curDate.after(latest)){
                latest = curDate;
            }
        }
        if (lastCommitDate.isEmpty()){
            lastCommitDate.add(DateUtil.getDate(latest));
        }else if (DateUtil.formatDate(lastCommitDate.get(0)) == null
                || DateUtil.formatDate(lastCommitDate.get(0)).before(latest)){
            lastCommitDate.set(0, DateUtil.getDate(latest));
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

    private static List<RDFNode> getRepositoryNodes(PVBModel model, Resource tool) {
        return model.getProperties(tool, PROVERB.P_REPOSITORY);
    }

    public static List<Repository> getPaperEntities(PVBModel model, Resource tool) {
        var nodes = getRepositoryNodes(model, tool);
        var repos = new ArrayList<Repository>(nodes.size());
        for (var node : nodes){
            var dto = new Repository();
            model.getProperty((Resource) node, PROVERB.P_NAME).ifPresent(dto::setName);
            model.getProperty((Resource) node, PROVERB.P_ABSTRACT).ifPresent(dto::setAbs);
            model.getProperty((Resource) node, PROVERB.P_LAST_COMMIT_DATE).ifPresent(dto::setLastCommitDate);
            repos.add(dto);
        }
        return repos;
    }
}
