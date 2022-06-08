package nl.utwente.proverb.domain.dto.crossref;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CrossRefDTO {

    private String status;

    private Message message;
}