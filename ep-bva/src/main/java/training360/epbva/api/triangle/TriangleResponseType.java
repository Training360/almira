package training360.epbva.api.triangle;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://training360.com/epbva/triangle")
public enum TriangleResponseType {

    EQUILATERAL, INVALID, ISOSCELES, SCALENE
}
