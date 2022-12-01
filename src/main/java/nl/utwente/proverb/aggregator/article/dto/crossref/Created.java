package nl.utwente.proverb.aggregator.article.dto.crossref;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class Created {

    @JsonAlias("date-time")
    private Date time;
}