package nl.utwente.proverb.evaluator;

import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.evaluator.service.EvaluateService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Log4j2
@Controller
public class EvaluateController {

    @Resource
    private EvaluateService evaluateService;

    //public Map<String, Integer> evaluate() {
//
    //    var templates = evaluateService.getTemplatesFromGitHub();
    //    HashMap<String, Integer> results = new HashMap<>(templates.size());
    //    for (var template : templates){
    //        switch (template.getRelationship()){
    //            case "sameAs":
    //                var num = evaluateService.handleSameAsRelationship(template);
    //                results.put(template.getName(), num);
    //                break;
    //            default:
    //                log.error("Unsupported relationships");
    //                break;
    //        }
    //    }
    //    return results;
    //}
}
