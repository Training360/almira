package training360.epbva;

import java.util.ArrayList;
import java.util.List;

public class CreditAssessment {

    public void isNumber(String value, String name, List<String> errors) {
        try {
            Integer.parseInt(value);
        }
        catch (NumberFormatException ne) {
            errors.add(String.format("A %s nem szám!", name));
            return;
        }
    }

    private void isValid(int i, String name, int min, int max, List<String> errors) {
        if (i < min) {
            errors.add(String.format("A %s minimális értéke %d!", name, min));
        }
        else if (i > max) {
            errors.add(String.format("A %s maximális értéke %d!", name, max));
        }
    }

    public void validate(int mortgage, int valueOfTheProperty, List<String> errors) {
        isValid(mortgage, "jelzáloghitel összege", 50_000, 10_000_000, errors);
        isValid(valueOfTheProperty, "ingatlan értéke", 250_000, 50_000_000, errors);
    }

    public WorkflowType calculateWorkflowType(int mortgageParam, int valueOfThePropertyParam) {
        int mortgage = round(mortgageParam);
        int valueOfTheProperty = round(valueOfThePropertyParam);
        return (mortgage >= 5_000_000 || valueOfTheProperty >= 10_000_000 ? WorkflowType.SENIOR : WorkflowType.NORMAL);
    }

    private int round(int i) {
        return (int) Math.round(i / 1000.0) * 1000;
    }

    public static void main(String[] args) {
        System.out.println(new CreditAssessment().round(49_999));
    }
}
