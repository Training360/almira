package training360.epbva.api.triangle;

import javax.xml.ws.WebFault;

@WebFault(name="TriangleFault", targetNamespace = "http://training360.com/epbva/triangle")
public class TriangleException extends Exception {

    private TriangleFault triangleFault;

    public TriangleException(TriangleFault triangleFault) {
        super(triangleFault.getMessages().get(0));
        this.triangleFault = triangleFault;
    }

    public TriangleFault getTriangleFault() {
        return triangleFault;
    }

    public void setTriangleFault(TriangleFault triangleFault) {
        this.triangleFault = triangleFault;
    }
}
