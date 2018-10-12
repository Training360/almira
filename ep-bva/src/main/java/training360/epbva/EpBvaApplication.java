package training360.epbva;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training360.epbva.api.credit.CreditAssessmentEndpoint;
import training360.epbva.api.triangle.TriangleEndpoint;

import javax.xml.ws.Endpoint;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/")
public class EpBvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpBvaApplication.class, args);
	}

	@Autowired
	private Bus bus;


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
        List<String> errors = new ArrayList<>();
        creditAssessment.isNumber(creditAssessmentRequest.getMortgage(), "jelzáloghitel összege", errors);
        creditAssessment.isNumber(creditAssessmentRequest.getValueOfTheProperty(), "ingatlan értéke", errors);

		if (!errors.isEmpty()) {
			return CreditAssessmentAnswer.error(errors);
		}

		int mortgage = Integer.parseInt(creditAssessmentRequest.getMortgage());
        int valueOfProperty = Integer.parseInt(creditAssessmentRequest.getValueOfTheProperty());
		creditAssessment.validate(mortgage, valueOfProperty, errors);

        if (!errors.isEmpty()) {
            return CreditAssessmentAnswer.error(errors);
        }

        return CreditAssessmentAnswer.workflowTypeOf(creditAssessment.calculateWorkflowType(mortgage, valueOfProperty));
	}

	@Bean
	public Endpoint triangleEndpointEndpoint(TriangleEndpoint triangleEndpoint) {
		EndpointImpl endpoint = new EndpointImpl(bus, triangleEndpoint);
		endpoint.publish("/triangle");
		return endpoint;
	}

    @Bean
    public Endpoint creditAssessmentEndpointEndpoint(CreditAssessmentEndpoint creditAssessmentEndpoint) {
        EndpointImpl endpoint = new EndpointImpl(bus, creditAssessmentEndpoint);
        endpoint.publish("/credit-assessment");
        return endpoint;
    }

}
