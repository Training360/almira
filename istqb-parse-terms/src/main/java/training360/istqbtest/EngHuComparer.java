package training360.istqbtest;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EngHuComparer {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, HuTerm.class);
        List<HuTerm> huTerms = objectMapper.readValue(new InputStreamReader(EngHuComparer.class.getResourceAsStream("/huterms.json"), StandardCharsets.UTF_8), type);
        System.out.println(huTerms);
        System.out.println("Magyarok száma:" + huTerms.size());
        Set<String> engFromHun = huTerms.stream().map(t -> t.getEngNames()).flatMap(l -> l.stream()).collect(Collectors.toSet());


        JavaType anotherType = objectMapper.getTypeFactory().constructCollectionType(List.class, Term.class);
        List<Term> engTerms = objectMapper.readValue(new InputStreamReader(EngHuComparer.class.getResourceAsStream("/terms.json"), StandardCharsets.UTF_8), anotherType);
        Set<String> engFromEng = engTerms.stream().map((t) -> {
            List<String> words = new ArrayList<>();
            words.add(t.getName());
            if (t.getSynonyms() != null) {
                words.addAll((t.getSynonyms()));
            }
            return words;
        }).flatMap(l -> l.stream()).collect(Collectors.toSet());

        System.out.println("Nincs az angolban:");
        engFromHun.stream().filter(v -> !engFromEng.contains(v)).forEach(System.out::println);

        System.out.println("Nincs a magyarban:");
        engFromEng.stream().filter(v -> !engFromHun.contains(v)).forEach(System.out::println);

        List<Term> filteredTems =
        engTerms.stream().filter((v) -> engFromHun.contains(v.getName())).collect(Collectors.toList());

        System.out.println(objectMapper.writeValueAsString(filteredTems));

    }
}
