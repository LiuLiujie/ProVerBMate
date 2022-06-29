package nl.utwente.proverb.service;


import nl.utwente.proverb.domain.dto.evaluate.EvaluatePatternTemplate;

import java.util.List;

public interface EvaluateService {

    List<EvaluatePatternTemplate> getTemplatesFromGitHub();

    EvaluatePatternTemplate getTemplatesFromString(String string);

    int handleSameAsRelationship(EvaluatePatternTemplate template);
}
