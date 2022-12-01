package nl.utwente.proverb.analyzer.pvbanalyzer.mdparser;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.util.*;

public class MDTool {

    @Setter
    @Getter
    private File file;

    @Setter
    @Getter
    private String name;

    @Getter
    private final List<String> titles = new ArrayList<>();

    private final Map<String, List<String>> tool = new HashMap<>();

    public @NonNull List<String> getProperty(String title){
        return Objects.requireNonNullElse(tool.get(title), new ArrayList<>());
    }

    public void addProperty(String title, List<String> value){
        tool.put(title, value);
        titles.add(title);
    }
}
