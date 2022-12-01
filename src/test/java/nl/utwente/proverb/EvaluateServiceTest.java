package nl.utwente.proverb;


import lombok.extern.log4j.Log4j2;
import nl.utwente.proverb.evaluator.service.EvaluateService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

@Log4j2
@SpringBootTest
class EvaluateServiceTest {

    @Resource
    EvaluateService evaluateService;

    private static final String FILE = "ac.json";

    @Test
    void handelSameAs(){
        try {
            String str = FileUtils.readFileToString(new File(FILE), Charset.defaultCharset());
            var template = evaluateService.getTemplatesFromString(str);
            evaluateService.handleSameAsRelationship(template);
        }catch (IOException e){
            log.error("readFileFail");
        }
    }
}
