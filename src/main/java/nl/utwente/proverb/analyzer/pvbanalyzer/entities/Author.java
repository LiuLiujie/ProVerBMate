package nl.utwente.proverb.analyzer.pvbanalyzer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@NoArgsConstructor
public class Author {

    private String given;

    private String family;

    private @Nullable String name;

    private @Nullable String email;

    private @Nullable String orcid;

    public @Nullable String getName() {

        if (name != null){
            return name;
        }

        if (given != null && family != null){
            return family + " " +given;
        }

        return null;
    }
}
