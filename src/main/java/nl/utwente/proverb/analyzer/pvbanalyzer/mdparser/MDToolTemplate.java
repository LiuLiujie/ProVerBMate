package nl.utwente.proverb.analyzer.pvbanalyzer.mdparser;

public class MDToolTemplate {

    private MDToolTemplate() { }

    public static final String PREFIX = "#### ";

    public static final String FULL_NAME = PREFIX + "Name:";

    public static final String DOMAIN = PREFIX + "Application domain/field:";

    public static final String TYPE = PREFIX + "Type of tool (e.g. model checker, test generator):";

    public static final String INPUT_THING = PREFIX + "Expected input thing:";

    public static final String INPUT_FORMAT = PREFIX + "Expected input format:";

    public static final String OUTPUT = PREFIX + "Expected output:";

    public static final String INTERNAL = PREFIX + "Internals (tools used, frameworks, techniques, paradigms, ...):";

    public static final String COMMENT = PREFIX + "Comments:";

    public static final String URIS = PREFIX + "URIs (github, websites, etc.):";

    public static final String LAST_COMMIT_DATE = PREFIX + "Last commit date:";

    public static final String LAST_PUB_DATE = PREFIX + "Last publication date:";

    public static final String PAPERS = PREFIX + "List of related papers:";

    public static final String RELA_TOOLS = PREFIX + "Related tools (tools mentioned or compared to in the paper):";

    public static final String META = PREFIX + "Meta";
}
