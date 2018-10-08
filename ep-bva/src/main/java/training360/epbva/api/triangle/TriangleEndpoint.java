package training360.epbva.api.triangle;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import training360.epbva.TriangleDecision;
import training360.epbva.TriangleType;

import java.util.List;

@Endpoint
public class TriangleEndpoint {

    private static final String NAMESPACE_URI = "http://training360.com/epbva/triangle";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "triangleRequest")
    @ResponsePayload
    public TriangleResponse classifyTriangle(@RequestPayload TriangleRequest triangleRequest)
        throws TriangleException {
        TriangleDecision triangleDecision = new TriangleDecision();

        List<String> errors = triangleDecision.validate(triangleRequest.getA(), triangleRequest.getB(), triangleRequest.getC());
        if (!errors.isEmpty()) {
            TriangleFault triangleFault = new TriangleFault();
            triangleFault.setMessages(errors);
            throw new TriangleException(triangleFault);
        }

        TriangleType triangleType = triangleDecision.classify(triangleRequest.getA(), triangleRequest.getB(), triangleRequest.getC());
        TriangleResponseType triangleResponseType = TriangleResponseType.valueOf(triangleType.name());
        TriangleResponse response = new TriangleResponse();
        response.setTriangleResponseType(triangleResponseType);
        return response;
    }
}
