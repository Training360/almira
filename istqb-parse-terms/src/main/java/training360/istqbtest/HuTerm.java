package training360.istqbtest;

import java.util.List;

public class HuTerm {

    private String name;

    private List<String> engNames;

    private List<String> seeAlso;

    private List<String> ref;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEngNames() {
        return engNames;
    }

    public void setEngNames(List<String> engNames) {
        this.engNames = engNames;
    }

    public List<String> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(List<String> seeAlso) {
        this.seeAlso = seeAlso;
    }

    public List<String> getRef() {
        return ref;
    }

    public void setRef(List<String> ref) {
        this.ref = ref;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "HuTerm{" +
                "name='" + name + '\'' +
                ", engNames=" + engNames +
                ", seeAlso=" + seeAlso +
                ", ref=" + ref +
                ", desc='" + desc + '\'' +
                '}';
    }
}
