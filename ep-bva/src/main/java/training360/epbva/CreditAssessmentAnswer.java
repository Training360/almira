package training360.epbva;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditAssessmentAnswer {

    private List<String> errors;

    private WorkflowType workflowType;

    public static CreditAssessmentAnswer error(List<String> answers) {
        return new CreditAssessmentAnswer(answers);
    }

    public static CreditAssessmentAnswer workflowTypeOf(WorkflowType workflowType) {
        return new CreditAssessmentAnswer(workflowType);
    }

    private CreditAssessmentAnswer(List<String> errors) {
        this.errors = errors;
    }

    private CreditAssessmentAnswer(WorkflowType workflowType) {
        this.workflowType = workflowType;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public WorkflowType getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(WorkflowType workflowType) {
        this.workflowType = workflowType;
    }
}
