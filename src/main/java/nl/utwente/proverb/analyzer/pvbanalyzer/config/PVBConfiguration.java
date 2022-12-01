package nl.utwente.proverb.analyzer.pvbanalyzer.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PVBConfiguration {

    @Getter
    private boolean loadRelatedPapers = true;
    @Getter
    private boolean loadPaperAllAuthors = true;
    @Getter
    private boolean loadPaperMainAuthor = true;
    @Getter
    private boolean loadRepositories = true;
    @Getter
    private boolean loadRepoLastCommitTime = true;
    @Getter
    private boolean loadRepoContributors = true;
}
