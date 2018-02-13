package training360.epbva;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TriangleAnswer {

    private List<String> errors;

    private TriangleType type;

    public static TriangleAnswer error(List<String> answers) {
        return new TriangleAnswer(answers);
    }

    public static TriangleAnswer typeOf(TriangleType triangleType) {
        return new TriangleAnswer(triangleType);
    }

    private TriangleAnswer(List<String> errors) {
        this.errors = errors;
    }

    private TriangleAnswer(TriangleType type) {
        this.type = type;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public TriangleType getType() {
        return type;
    }

    public void setType(TriangleType type) {
        this.type = type;
    }
}
