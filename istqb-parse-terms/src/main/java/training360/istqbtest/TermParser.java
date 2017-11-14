package training360.istqbtest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TermParser {

    public static void main(String[] args) throws Exception {
        List<Term> terms = new TermParser().parse(TermParser.class.getResourceAsStream("/Foundation_Terms.txt"));
        System.out.println(terms);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String s = objectMapper.writeValueAsString(terms);
        System.out.println(s);
    }

    private List<Term> parse(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .collect(TermCollector::new, TermCollector::accumulate, TermCollector::combine).getTerms();
    }


}
