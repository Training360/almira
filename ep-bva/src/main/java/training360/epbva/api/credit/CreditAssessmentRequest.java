package training360.epbva.api.credit;

public class CreditAssessmentRequest {

    private int mortgage;

    private int valueOfTheProperty;

    public int getMortgage() {
        return mortgage;
    }

    public void setMortgage(int mortgage) {
        this.mortgage = mortgage;
    }

    public int getValueOfTheProperty() {
        return valueOfTheProperty;
    }

    public void setValueOfTheProperty(int valueOfTheProperty) {
        this.valueOfTheProperty = valueOfTheProperty;
    }
}
