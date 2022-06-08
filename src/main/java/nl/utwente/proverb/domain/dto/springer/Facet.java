package nl.utwente.proverb.domain.dto.springer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Facet {

    private String name;

    private List<Value> values;
}