package training360.epbva.api.credit;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://training360.com/epbva/credit")
public enum CreditAssessmentResponseType {

    NORMAL, SENIOR
}
