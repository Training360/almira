package training360.epbva.api.triangle;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://training360.com/epbva/triangle")
public class TriangleResponse {

    private TriangleResponseType triangleResponseType;

    public TriangleResponseType getTriangleResponseType() {
        return triangleResponseType;
    }

    public void setTriangleResponseType(TriangleResponseType triangleResponseType) {
        this.triangleResponseType = triangleResponseType;
    }
}
