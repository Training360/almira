package training360.istqbtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TermCollector {

    private enum Status {NONE, NAME, REF, SEE_ALSO, SYNONYMS, DESC}

    private List<Term> terms = new ArrayList<>();

    private Term actualTerm;

    private Status status = Status.NONE;

    public void accumulate(String line) {
        line = line.trim();
        if (line.equals("")) {
            return;
        }
        if (status == Status.NONE || status == Status.DESC) {
                actualTerm = new Term();
                actualTerm.setName(line.trim());
                terms.add(actualTerm);
                status = Status.NAME;
        }
        else if (line.startsWith("Ref:")) {
            actualTerm.setRef(parseRef(line));
            status = Status.REF;
            // A Ref sor is tartalmazhat See Also-t
            if (line.contains("See Also:")) {
                actualTerm.setSeeAlso(parseSeeAlso(line));
                status = Status.SEE_ALSO;
            }
        }
        else if (line.contains("See Also:")) {
            actualTerm.setSeeAlso(parseSeeAlso(line));
            status = Status.SEE_ALSO;
        }
        else if (line.startsWith("Synonyms:")) {
            actualTerm.setSynonyms(parseSynonyms(line));
            status = Status.SYNONYMS;
        }
        else {
            actualTerm.setDesc(line);
            status = Status.DESC;
        }

    }

    public void combine(TermCollector termCollector) {
        terms.addAll(termCollector.terms);
    }

    private List<String> parseSynonyms(String line) {
        int start = line.indexOf("Synonyms:") + "Synonyms:".length();
        return split(line.substring(start));
    }

    private List<String> parseSeeAlso(String line) {
        int start = line.indexOf("See Also:") + "See Also:".length();
        return split(line.substring(start));
    }

    private List<String> split(String s) {
        return Arrays.stream(s.split(",")).map(String::trim).filter((i) -> !i.equals("")).collect(Collectors.toList());
    }

    private String parseRef(String line) {
        int start = "Ref:".length();
        if (line.contains("See Also:")) {
            return line.substring(start, line.indexOf("See Also:")).trim();
        }
        else {
            return line.substring(start).trim();
        }
    }

    public List<Term> getTerms() {
        return terms;
    }
}
