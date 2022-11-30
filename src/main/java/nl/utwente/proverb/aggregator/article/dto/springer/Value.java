package nl.utwente.proverb.aggregator.article.dto.springer;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Value {

    @JsonAlias("value")
    private String property;

    private String count;
}
