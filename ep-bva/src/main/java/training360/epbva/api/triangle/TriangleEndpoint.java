package training360.epbva.api.triangle;

import org.springframework.stereotype.Service;
import training360.epbva.TriangleDecision;
import training360.epbva.TriangleType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@WebService
@Service
public class TriangleEndpoint {

    @WebMethod
    @WebResult(name = "triangleType")
    public TriangleType classifyTriangle(@WebParam(name = "triangle") @XmlElement(required = true) Triangle triangle) {
        TriangleDecision triangleDecision = new TriangleDecision();

        List<String> errors = triangleDecision.validate(triangle.getA(), triangle.getB(), triangle.getC());
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(errors.get(0));
        }

        TriangleType triangleType = triangleDecision.classify(triangle.getA(), triangle.getB(), triangle.getC());
        return triangleType;
    }
}
