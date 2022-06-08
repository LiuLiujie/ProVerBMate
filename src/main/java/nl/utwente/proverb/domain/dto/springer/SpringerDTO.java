package nl.utwente.proverb.domain.dto.springer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SpringerDTO {

    private List<Record> records;

    private List<Facet> facets;
}