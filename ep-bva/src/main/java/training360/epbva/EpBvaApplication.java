package training360.epbva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api/")
@EnableWs
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

	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
	}

	@Bean(name = "triangle")
	public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema locationsSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("TrianglePort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://training360.com/epbva/triangle");
		wsdl11Definition.setSchema(locationsSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema locationsSchema() {
		return new SimpleXsdSchema(new ClassPathResource("triangle.xsd"));
	}

}
