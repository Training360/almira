package training360.epbva;

import java.util.ArrayList;
import java.util.List;

public class TriangleDecision {

    private boolean isNumber(String edge, String name, List<String> errors) {
        try {
            Integer.parseInt(edge);
        }
        catch (NumberFormatException ne) {
            errors.add(String.format("A háromszög \"%s\" oldala nem szám!", name));
            return false;
        }
        return true;
    }

    private boolean validEdge(int edge, String name, List<String> errors) {
        if (edge < 1) {
            errors.add(String.format("A háromszög \"%s\" oldala kisebb, mint 1!", name));
            return false;
        }
        else if (edge >= 200) {
            errors.add(String.format("A háromszög \"%s\" oldala nagyobb, mint 200!", name));
            return false;
        }
        return true;
    }

    public List<String> validate(String a, String b, String c) {
        List<String> errors = new ArrayList<>();
        if (isNumber(a, "a", errors)) {
            validEdge(Integer.parseInt(a), "a", errors);
        }
        if (isNumber(b, "b", errors)) {
            validEdge(Integer.parseInt(b), "b", errors);
        }
        if (isNumber(c, "c", errors)) {
            validEdge(Integer.parseInt(c), "c", errors);
        }
        return errors;
    }

    public List<String> validate(int a, int b, int c) {
        List<String> errors = new ArrayList<>();
        validEdge(a, "a", errors);
        validEdge(b, "b", errors);
        validEdge(c, "c", errors);
        return errors;
    }



    public TriangleType calculateType(String aParam, String bParam, String cParam) {
        int a = Integer.parseInt(aParam);
        int b = Integer.parseInt(bParam);
        int c = Integer.parseInt(cParam);
        return classify(a, b, c);
    }

    public TriangleType classify(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) return TriangleType.INVALID;
        if (a == b && b == c) return TriangleType.EQUILATERAL;
        if (a > b+c || c > b+a || b > a+c) return TriangleType.INVALID;
        if (b==c || a==b || c==a) return TriangleType.ISOSCELES;
        return TriangleType.SCALENE;
    }

}
