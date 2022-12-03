package nl.utwente.proverb.analyzer.pvbanalyzer.handlers;

public abstract class BaseHandler {

    /**
     * Enrich the original dataset without changing existed data
     */
    public abstract void enrichment();

    /**
     * Extract Information from Markdown files
     */
    public abstract void extractInfo();
}
