package training360.epbva;

import java.util.ArrayList;
import java.util.List;

public class TriangleDecision {

    private  void validateEdge(String edge, String name, List<String> errors) {
        int i = 0;
        try {
            i = Integer.parseInt(edge);
        }
        catch (NumberFormatException ne) {
            errors.add(String.format("A %s nem szám!", name));
            return;
        }
        if (i < 1) {
            errors.add(String.format("A %s kisebb, mint 1!", name));
        }
        else if (i > 200) {
            errors.add(String.format("A %s nagyobb, mint 200!", name));
        }
    }

    public List<String> validate(String a, String b, String c) {
        List<String> errors = new ArrayList<>();
        validateEdge(a, "a", errors);
        validateEdge(b, "b", errors);
        validateEdge(c, "c", errors);
        return errors;
    }

    public TriangleType calculateType(String aParam, String bParam, String cParam) {
        int a = Integer.parseInt(aParam);
        int b = Integer.parseInt(bParam);
        int c = Integer.parseInt(cParam);
        return classify(a, b, c);
    }

    private TriangleType classify(int a, int b, int c) {
        if (a <= 0 || b <= 0 || c <= 0) return TriangleType.INVALID;
        if (a == b && b == c) return TriangleType.EQUILATERAL;
        if (a >= b+c || c >= b+a || b >= a+c) return TriangleType.INVALID;
        if (b==c || a==b || c==a) return TriangleType.ISOSCELES;
        return TriangleType.SCALENE;
    }

}
