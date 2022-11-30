package nl.utwente.proverb.analyzer.pvbanalyzer.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Repository {

    private String name;

    private String abs;

    private List<Contributor> contributors;

    @Nullable
    private String lastCommitDate;

    private String url;

    @Getter
    @AllArgsConstructor
    public static class Contributor {

        private String name;

        private String url;
    }
}
