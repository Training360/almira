package training360.istqbtest;

import java.util.*;
import java.util.stream.Collectors;

public class HuTermCollector {

    private static final char ARROW = 61614;

    private static final List<String> CATEGORIES = Arrays.asList("ATM", "ATA", "F-AT", "F", "ATT", "EITP", "ETM", "ETAE");

    private static final List<String> SKIP = Arrays.asList("modell-alapú teszt", "tesztvégrehajtási réteg", "tesztfejlesztési terv");

    private static final Map<String, String> CORRECTIONS = new HashMap<>();

    static {
        CORRECTIONS.put("COTS", "commercial off-the-shelf (COTS)");
        CORRECTIONS.put("Commercial Off-TheShelf software", "commercial off-the-shelf (COTS)");
        CORRECTIONS.put("TDD", "test-driven development (TDD)");
        CORRECTIONS.put("action word driven testing", "action word-driven testing");
        CORRECTIONS.put("black box technique", "black-box test design technique");
        CORRECTIONS.put("black box test design technique", "black-box test design technique");
        CORRECTIONS.put("black box testing", "black-box testing");
        CORRECTIONS.put("data--driven testing", "data-driven testing");
        CORRECTIONS.put("experienced-based technique", "experience-based test design technique");
        CORRECTIONS.put("glass box testing", "glass-box testing");
        CORRECTIONS.put("test driven development", "test-driven development (TDD)");
    }

    private List<HuTerm> terms = new ArrayList<>();

    private StringBuilder defLine = null;

    private boolean prevCategory = false;

    public void accumulate(String line) {
        System.out.println("Processing line: " + line);
        if (line.trim().equals("")) {
            return;
        }
        if ((hasDefinition(line) && !prevCategory) || (startsWithCategory(line) && !prevCategory)) {
            processPrevTerm();
            defLine = new StringBuilder();
        }
        prevCategory = startsWithCategory(line);
        defLine.append(line).append(" ");

    }

    private boolean startsWithCategory(String line) {
        for (String category: CATEGORIES) {
            if (line.startsWith(category)) {
                return true;
            }
        }
        return false;
    }

    private String removeAndSetCategories(String line, List<String> categories) {
        line = line.trim();
        if (line.startsWith(",")) {
            line = line.substring(1);
        }
        for (String category: CATEGORIES) {
            if (line.startsWith(category)) {
                categories.add(category);
                return removeAndSetCategories(line.substring(category.length()), categories);
            }
        }

        return line.trim();
    }

    private void processPrevTerm() {
        if (defLine == null) {
            return;
        }

        String line = defLine.toString();

        System.out.println("Process term: " + line);

        List<String> categories = new ArrayList<>();
        line = removeAndSetCategories(line, categories);

        if (!categories.contains("F")) {
            return;
        }

        System.out.println("Process fundamental:" + line);

        String name = line.substring(0, line.indexOf(":"));

        if (SKIP.contains(name)) {
            return;
        }

        String desc = processDesc(line);

        if (desc.startsWith("lásd")) {
            return;
        }

        List<String> engNames = processEngNames(line);

        HuTerm huTerm = new HuTerm();
        huTerm.setName(name);
        huTerm.setDesc(desc);
        huTerm.setEngNames(engNames);
        System.out.println("Add term: " + huTerm);
        terms.add(huTerm);
    }

    private String processDesc(String line) {
        int start = line.indexOf(":") + 1;
        int end = line.indexOf(ARROW);
        String desc = line.substring(start, end);

        if (desc.contains("[")) {
            desc = desc.substring(0, desc.indexOf("["));
        }
        if (desc.contains("Lásd még:")) {
            desc = desc.substring(0, desc.indexOf("Lásd még:"));
        }

        desc = desc.replace("\f", "");

        desc = desc.trim();

        return desc;
    }


    private List<String> processEngNames(String line) {
        List<String> engNames = split(line.substring(line.indexOf(ARROW) + 1));
        return engNames.stream().map((i) -> CORRECTIONS.getOrDefault(i, i)).collect(Collectors.toList());
    }

    private boolean hasDefinition(String line) {
        return line.contains(":")
                && !line.startsWith("pl.:")
                && !line.contains(". Példák:")
                && !line.contains(", például:")
                && !line.contains(". Például:")
                && !line.contains("adminisztratív irányítása: ")
                && !line.contains("képessége van: ")

                && (line.indexOf("még:") + 3 != line.indexOf(":"));

    }

    public void combine(HuTermCollector huTermCollector) {
        terms.addAll(huTermCollector.terms);
    }

    public List<HuTerm> getTerms() {
        return terms;
    }

    private List<String> split(String s) {
        return Arrays.stream(s.split(",")).map(String::trim).filter((i) -> !i.equals("")).collect(Collectors.toList());
    }

}
