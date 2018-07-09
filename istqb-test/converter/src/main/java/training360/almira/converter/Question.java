package training360.almira.converter;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String folder;

    private String title;

    private String text = "";

    private List<Answer> answers = new ArrayList<>();

    private List<File> files = new ArrayList<>();

    public String getFormattedTitle() {
        int start = title.indexOf(" ");
        int number = Integer.parseInt(title.substring(start + 1));
        return String.format("Q. %04d", number);
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
