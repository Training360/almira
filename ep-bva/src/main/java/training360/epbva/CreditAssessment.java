package training360.epbva;

import java.util.ArrayList;
import java.util.List;

public class CreditAssessment {

    public void validateNumber(String value, String name, int min, int max, List<String> errors) {
        int i = 0;
        try {
            i = Integer.parseInt(value);
        }
        catch (NumberFormatException ne) {
            errors.add(String.format("A %s nem szám!", name));
            return;
        }
        i = round(i);
        if (i < min) {
            errors.add(String.format("A %s minimális értéke %d!", name, min));
        }
        else if (i > max) {
            errors.add(String.format("A %s maximális értéke %d!", name, max));
        }
    }

    public List<String> validate(String mortgage, String valueOfTheProperty) {
        List<String> errors = new ArrayList<>();
        validateNumber(mortgage, "jelzáloghitel összege", 50_000, 10_000_000, errors);
        validateNumber(valueOfTheProperty, "ingatlan értéke", 250_000, 50_000_000, errors);
        return errors;
    }

    public WorkflowType calculateWorkflowType(String mortgageParam, String valueOfThePropertyParam) {
        int mortgage = round(Integer.parseInt(mortgageParam));
        int valueOfTheProperty = round(Integer.parseInt(valueOfThePropertyParam));
        return (mortgage >= 5_000_000 || valueOfTheProperty >= 10_000_000 ? WorkflowType.SENIOR : WorkflowType.NORMAL);
    }

    private int round(int i) {
        return (int) Math.round(i / 1000.0) * 1000;
    }

    public static void main(String[] args) {
        System.out.println(new CreditAssessment().round(49_999));
    }
}
