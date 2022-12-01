package nl.utwente.proverb.evaluator.service;


import nl.utwente.proverb.evaluator.dto.EvaluatePatternTemplate;

public interface EvaluateService {

    //List<EvaluatePatternTemplate> getTemplatesFromGitHub();

    EvaluatePatternTemplate getTemplatesFromString(String string);

    int handleSameAsRelationship(EvaluatePatternTemplate template);
}
