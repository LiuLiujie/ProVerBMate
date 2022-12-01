package nl.utwente.proverb.analyzer.pvbanalyzer;

import nl.utwente.proverb.analyzer.pvbanalyzer.config.PVBConfiguration;
import nl.utwente.proverb.analyzer.pvbanalyzer.handlers.BaseHandler;
import nl.utwente.proverb.analyzer.pvbanalyzer.handlers.PaperHandler;
import nl.utwente.proverb.analyzer.pvbanalyzer.handlers.RepoHandler;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.ToolParser;
import nl.utwente.proverb.ontology.service.OntologyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
@Component
public class PVBController implements BaseHandler {

    @Value("${tools}")
    private String toolsPath;

    @Resource
    private OntologyService ontologyService;

    @Resource
    private PVBConfiguration pvbConfiguration;

    private final List<BaseHandler> subAnalyzers = new ArrayList<>();

    private List<File> loadTools(){
        var path = new File(toolsPath);
        if (!path.isDirectory()){
            throw new IllegalArgumentException("Invalid tools path");
        }
        var rootFiles = path.listFiles();
        if (rootFiles == null || rootFiles.length == 0){
            throw new IllegalArgumentException("Invalid tools path");
        }
        return getFiles(new ArrayList<>(List.of(rootFiles)));
    }

    private List<File> getFiles(List<File> rootFiles){

        var newFiles = new ArrayList<File>();
        for (File file : rootFiles){
            if (file.isDirectory()){
                var fs = file.listFiles();
                if (fs != null){
                    newFiles.addAll(getFiles(new ArrayList<>(List.of(fs))));
                }
            }
            if (file.isFile()){
                newFiles.add(file);
            }
        }
        return newFiles;
    }

    @Override
    public void autoEnrichment() {
        var tools = this.loadTools();
        for (var tool : tools){
            ToolParser parser = new ToolParser(tool, pvbConfiguration);
            MDTool mdTool = parser.parse();
            subAnalyzers.add(new PaperHandler(ontologyService, mdTool));
            subAnalyzers.add(new RepoHandler(ontologyService, mdTool));
            subAnalyzers.forEach(BaseHandler::autoEnrichment);
            parser.write(mdTool);
        }
    }

    @Override
    public void reGeneration() {
        subAnalyzers.forEach(BaseHandler::reGeneration);
    }
}
