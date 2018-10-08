package training360.epbva.api.credit;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://training360.com/epbva/credit")
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
