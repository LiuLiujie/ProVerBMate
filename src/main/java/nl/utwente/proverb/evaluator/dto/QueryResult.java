package nl.utwente.proverb.evaluator.dto;

import lombok.Data;
import org.apache.jena.rdf.model.RDFNode;

import java.util.List;
import java.util.Map;

@Data
public class QueryResult {

    List<String> properties;

    Map<String, List<RDFNode>> values;
}
