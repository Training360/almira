package training360.epbva.api.triangle;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "http://training360.com/epbva/triangle")
public class TriangleRequest {

    private int a;

    private int b;

    private int c;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
}
