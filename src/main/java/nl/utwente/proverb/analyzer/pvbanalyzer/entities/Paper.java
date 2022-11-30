package nl.utwente.proverb.analyzer.pvbanalyzer.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Paper {

    String title;

    String abs;

    String doiURL;

    List<Author> authors;
}
