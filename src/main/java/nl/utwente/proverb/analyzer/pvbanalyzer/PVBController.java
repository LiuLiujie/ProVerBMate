package nl.utwente.proverb.analyzer.pvbanalyzer;

import nl.utwente.proverb.analyzer.pvbanalyzer.handlers.BaseHandler;
import nl.utwente.proverb.analyzer.pvbanalyzer.handlers.RepoHandler;
import nl.utwente.proverb.analyzer.pvbanalyzer.mdparser.MDTool;
import nl.utwente.proverb.ontology.PVBModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class PVBController implements BaseHandler {

    private final PVBModel model;

    private final List<BaseHandler> subAnalyzers = new ArrayList<>();

    public PVBController(PVBModel model, MDTool mdTool){
        this.model = model;
        //subAnalyzers.add(new PaperAnalyzer(model, mdTool));
        subAnalyzers.add(new RepoHandler(model, mdTool));
    }

    @Override
    public void autoEnrichment() {
        subAnalyzers.forEach(BaseHandler::autoEnrichment);
    }

    @Override
    public void reGeneration() {
        subAnalyzers.forEach(BaseHandler::reGeneration);
    }
}
