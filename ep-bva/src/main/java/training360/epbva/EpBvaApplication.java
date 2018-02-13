package training360.epbva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/")
public class EpBvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpBvaApplication.class, args);
	}

	@RequestMapping("/triangle")
	public TriangleAnswer triangle(@RequestBody Triangle triangle) {
		TriangleDecision triangleDecision = new TriangleDecision();
		List<String> errors = triangleDecision.validate(triangle.getA(), triangle.getB(), triangle.getC());
		if (!errors.isEmpty()) {
			return TriangleAnswer.error(errors);
		}
		else {
			return TriangleAnswer.typeOf(triangleDecision.calculateType(triangle.getA(), triangle.getB(), triangle.getC()));
		}
	}

	@RequestMapping("/credit-assessment")
	public CreditAssessmentAnswer creditAssessment(@RequestBody CreditAssessmentRequest creditAssessmentRequest) {
		CreditAssessment creditAssessment = new CreditAssessment();
		List<String> errors = creditAssessment.validate(creditAssessmentRequest.getMortgage(), creditAssessmentRequest.getValueOfTheProperty());
		if (!errors.isEmpty()) {
			return CreditAssessmentAnswer.error(errors);
		}
		else {
			return CreditAssessmentAnswer.workflowTypeOf(creditAssessment.calculateWorkflowType(creditAssessmentRequest.getMortgage(), creditAssessmentRequest.getValueOfTheProperty()));
		}
	}
}
