package training360.epbva.api.credit;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://training360.com/epbva/credit")
public class CreditAssessmentResponse {

    private CreditAssessmentResponseType creditAssesmentResponseType;

    public CreditAssessmentResponseType getCreditAssesmentResponseType() {
        return creditAssesmentResponseType;
    }

    public void setCreditAssesmentResponseType(CreditAssessmentResponseType creditAssesmentResponseType) {
        this.creditAssesmentResponseType = creditAssesmentResponseType;
    }
}
