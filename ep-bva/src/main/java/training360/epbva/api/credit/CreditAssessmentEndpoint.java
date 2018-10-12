package training360.epbva.api.credit;

import org.springframework.stereotype.Service;
import training360.epbva.CreditAssessment;
import training360.epbva.WorkflowType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@WebService
@Service
public class CreditAssessmentEndpoint {

    @WebMethod
    @WebResult(name = "workflowType")
    public WorkflowType calculateWorkflowType(@WebParam(name = "creditAssessmentRequest") @XmlElement(required = true) CreditAssessmentRequest creditAssessmentRequest) {
        CreditAssessment creditAssessment = new CreditAssessment();
        List<String> errors = new ArrayList<>();

        creditAssessment.validate(creditAssessmentRequest.getMortgage(), creditAssessmentRequest.getValueOfTheProperty(), errors);

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.get(0));
        }

        return creditAssessment.calculateWorkflowType(creditAssessmentRequest.getMortgage(), creditAssessmentRequest.getValueOfTheProperty());
    }
}
