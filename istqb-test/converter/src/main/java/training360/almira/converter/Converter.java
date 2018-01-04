package training360.almira.converter;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Converter {

    private TemplateEngine templateEngine;

    public static void main(String[] args) {
        String result = new Converter().convert();
        System.out.println(result);
    }

    private String convert() {
        Context context = new Context();
        List<Question> questions = readQuestions();
        context.setVariable("questions", questions);
        return templateEngine.process("template", context);
    }

    private List<Question> readQuestions() {
        Map<String, String> answers = readAnswers();


        List<Question> questions = new ArrayList<>();
        String state = "inquestion";
        java.io.File f = new java.io.File("..\\txt\\questions.txt");
        System.out.println(f.getAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = null;
            int index = 0;
            Question question = null;
            Answer answer = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Q. ") && line.contains(":")) {
                    // Új kérdés
                    question = new Question();
                    questions.add(question);
                    int end = line.indexOf(":");
                    String title = line.substring(0, end);
                    question.setTitle(title);
                    question.setText(line);
                    state = "inquestion";
                }
                else if (line.startsWith("A. ") || line.startsWith("B. ") || line.startsWith("C. ") || line.startsWith("D. ") || line.startsWith("E. ") || line.startsWith("F. ")) {
                    answer = new Answer();
                    question.getAnswers().add(answer);
                    answer.setText(line);
                    if (answers.get(question.getTitle()).equals(line.substring(0, 1))) {
                        answer.setValid(true);
                    }
                    state = "inanswer";
                }
                else if (state.equals("inquestion") || state.equals("inpre")) {
                    if (line.startsWith("```") && !state.equals("inpre")) {
                        question.setText(question.getText() + "<pre>");
                        state = "inpre";
                    }
                    else if (line.startsWith("```") && state.equals("inpre")) {
                        question.setText(question.getText() + "</pre>");
                        state = "inquestion";
                    }
                    else {
                        question.setText(question.getText() + "<br />\n" + line);
                    }
                }
                else if (state.equals("inanswer")) {
                    if (!line.trim().equals("")) {
                        answer.setText(answer.getText() + "\n" + line);
                    }
                }
                else {
                    throw new IllegalStateException("Not possible");
                }
                index++;
            }
            return questions;
        } catch (IOException e) {
            throw new IllegalStateException("Error reading file", e);
        }
    }

    private Map<String,String> readAnswers() {
        Map<String,String> answers = new HashMap<>();
        java.io.File f = new java.io.File("..\\txt\\answers.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int i = line.indexOf(":");
                String key = line.substring(0, i);
                String value = line.substring(i + 2);
                answers.put(key, value);
            }
            System.out.println(answers);
            return answers;
        } catch (IOException e) {
            throw new IllegalStateException("Error reading file", e);
        }
    }

    public Converter() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setTemplateMode(TemplateMode.XML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".xml");

    }
}
