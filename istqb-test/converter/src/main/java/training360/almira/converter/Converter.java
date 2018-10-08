package training360.almira.converter;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Converter {

    private TemplateEngine templateEngine;

    private static final String PREFIX = "../txt/";

    private static final String UTF8_BOM = "\uFEFF";

    private static final Path CONTENT_PATH = Paths.get("target", "content");

    public static void main(String[] args) {
        new Converter().convert();
    }

    private void convert() {
        createDirectories();
        List<Question> questions = readQuestions();

        writeQuestions(questions);

        createZip();
    }

    private void createZip() {
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(CONTENT_PATH.resolve("content.zip")))) {
            Files.walk(CONTENT_PATH.resolve("_files")).filter((p -> Files.isRegularFile(p)))
                .forEach(p -> {
                    try {
                        String path = p.toString().substring(p.toString().indexOf("_files"));

                        ZipEntry e = new ZipEntry(path);
                        out.putNextEntry(e);
                        Files.copy(p, out);
                        out.closeEntry();
                    }
                    catch (IOException ioe) {
                        throw new IllegalArgumentException("Cannot put file", ioe);
                    }
                });

            Files.walk(CONTENT_PATH).filter(p -> p.getFileName().toString().equals("quizitems.xml"))
                    .sorted(Comparator.comparing(p -> p.toString().length()))
                    .forEach(p -> {
                        try {
                            String path = p.toString().substring(p.toString().indexOf("content") + 8);

                            ZipEntry e = new ZipEntry(path);
                            out.putNextEntry(e);
                            Files.copy(p, out);
                            out.closeEntry();
                        }
                        catch (IOException ioe) {
                            throw new IllegalArgumentException("Cannot put file", ioe);
                        }
                            }
                    );

        }
        catch (IOException ioe) {
            throw new IllegalArgumentException("Cannot create content.zip", ioe);
        }
    }

    private void writeQuestions(List<Question> questions) {
        Map<String, List<Question>> questionsByFolder = questions
                .stream()
                .collect(Collectors.groupingBy(q -> Optional.ofNullable(q.getFolder()).orElse("")));
        questionsByFolder.forEach((folder, questionsInFolder) -> {
            Path path = CONTENT_PATH.resolve(folder);
            try {
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
            }
            catch (IOException ioe) {
                throw new IllegalStateException("Cannot create folder: " + path, ioe);
            }

            path = path.resolve("quizitems.xml");
            Context context = new Context();
            context.setVariable("questions", questionsInFolder);
            try {
                templateEngine.process("template", context, Files.newBufferedWriter(path));
            }
            catch (IOException ioe) {
                throw new IllegalStateException("Cannot write file: " + path, ioe);
            }

        });
    }


    private void createDirectories() {
        try {
            Files.createDirectories(CONTENT_PATH);
        }
        catch (IOException ioe) {
            throw new IllegalStateException("Cannot create directories: " + CONTENT_PATH, ioe);
        }
    }

    private List<Question> readQuestions() {
        Map<String, String> answers = readAnswers();


        List<Question> questions = new ArrayList<>();
        String state = "inquestion";
        Path p = getPath(PREFIX + "questions.txt");
        System.out.println(p.toAbsolutePath());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(p.toFile()), "UTF-8"))) {
            String line;
            Question question = null;
            Answer answer = null;
            int index = 1;
            StringBuilder questionText = new StringBuilder();
            boolean prevEmpty = false;
            while ((line = reader.readLine()) != null) {
                line = removeBom(line);
                if (line.startsWith("Q. ") && line.contains(":")) {
                    // Új kérdés
                    question = new Question();
                    questions.add(question);
                    int end = line.indexOf(":");
                    String title = line.substring(0, end);

                    int start = title.indexOf(" ");
                    int number = Integer.parseInt(title.substring(start + 1));
//                    if (number != index) {
//                        throw new IllegalStateException("Missing number: " + index + "  " + number);
//                    }

                    question.setTitle(title);
                    line = replaceSpec(line);

                    if (hasFolder(line)) {
                        question.setFolder(getFolder(line));
                        line = filterFolder(line);
                    }

                    questionText.append(line).append("<br />\n");
                    state = "inquestion";
                    index++;
                    prevEmpty = false;
                }
                else if (line.startsWith("A. ") || line.startsWith("B. ") || line.startsWith("C. ") || line.startsWith("D. ") || line.startsWith("E. ") || line.startsWith("F. ")) {

                    if (line.startsWith("A")) {
                        // Válasz kezdődik, kérdést lezárjuk
                        question.setText(questionText.toString());
                        questionText = new StringBuilder();
                    }

                    answer = new Answer();
                    question.getAnswers().add(answer);
                    answer.setText(line);
                    if (answers.get(question.getTitle()).equals(line.substring(0, 1))) {
                        answer.setValid(true);
                    }
                    state = "inanswer";
                }
                else if (state.equals("inpre")) {
                    line = replaceSpec(line);
                    if (line.startsWith("```")) {
                        questionText.append("</pre>\n");
                        state = "inquestion";
                    }
                    else {
                        questionText.append(line).append("\n");
                    }
                }
                else if (state.equals("inquestion")) {
                    line = replaceSpec(line);
                    if (line.startsWith("```")) {
                        questionText.append("<pre>");
                        state = "inpre";
                        prevEmpty = false;
                    }
                    else if (line.startsWith("![]")) {
                        // Kép
                        String filename = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                        File file = new File();
                        String basename = filename.substring(filename.indexOf("/") + 1);
                        file.setName(basename);
                        String id = UUID.randomUUID().toString();
                        file.setId(id);
                        file.setPath("_files/" + id + "/" + basename);
                        question.getFiles().add(file);

                        String img = generateImg(id, filename);
                        questionText.append(img).append("<br />\n");

                        copyFile(id, basename);
                        prevEmpty = false;
                    }
                    else if (line.trim().equals("")) {
                        if (!prevEmpty) {
                            // Dupla sortörések kiszűrése
                            questionText.append("<br />\n");
                        }
                        prevEmpty = true;
                    }
                    else {
                        questionText.append(line).append("<br />\n");
                        prevEmpty = false;
                    }
                }
                else if (state.equals("inanswer")) {
                    line = replaceSpec(line);
                    if (!line.trim().equals("")) {
                        answer.setText(answer.getText() + "\n" + line);
                    }
                }
                else {
                    throw new IllegalStateException("Not possible");
                }
            }
            return questions;
        } catch (IOException e) {
            throw new IllegalStateException("Error reading file", e);
        }
    }

    private String filterFolder(String line) {
        // Mögötte lévő space-t is filterelni kell
        return line.replace(line.substring(line.indexOf("("), line.indexOf(")") + 2), "");
    }

    private String getFolder(String line) {
        return line.substring(line.indexOf("(") + 1, line.indexOf(")"));
    }

    private boolean hasFolder(String line) {
        return line.substring(line.indexOf(":") + 2, line.indexOf(":") + 3).equals("(");
    }

    private String removeBom(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

    private void copyFile(String id, String basename) {
        Path source = getPath(PREFIX + "images/" + basename);
        Path destDir = CONTENT_PATH.resolve("_files").resolve(id);
        Path dest = destDir.resolve(basename);
        try {
            Files.createDirectories(destDir);
            Files.copy(source, dest);
        }
        catch (IOException ioe) {
            throw new IllegalStateException("Illegal copy.", ioe);
        }
    }

    private String generateImg(String id, String filename) {
        String basename = filename.substring(filename.lastIndexOf("/") + 1);
        return "<img src='File/DownloadPicture-" + id.replace("-", "") + "/Medium?downloadName=" + basename + "' alt='" + basename + "' data-file_id='" + id + "' />";
    }

    private String replaceSpec(String s) {
        // Sajnos utext-tel kell, hogy ő ne cserélje le pl. az img tag-eket, de így viszont a spec karaktereket
        // nekünk kell
        s = s.replace("&", "&amp;");
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        return s;

    }

    private Map<String,String> readAnswers() {
        Map<String,String> answers = new HashMap<>();
        Path f = getPath(PREFIX + "answers.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(f.toFile()))) {
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

    private static Path getPath(String s) {
        return Paths.get(s.replace("/", FileSystems.getDefault().getSeparator()));
    }
}
