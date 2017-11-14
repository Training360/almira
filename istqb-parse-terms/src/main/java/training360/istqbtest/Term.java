package training360.istqbtest;

import java.util.List;

public class Term {

    private String name;

    private String ref;

    private List<String> seeAlso;

    private List<String> synonyms;

    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public List<String> getSeeAlso() {
        return seeAlso;
    }

    public void setSeeAlso(List<String> seeAlso) {
        this.seeAlso = seeAlso;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Term{" +
                "name='" + name + '\'' +
                ", ref='" + ref + '\'' +
                ", seeAlso=" + seeAlso +
                ", synonyms=" + synonyms +
                ", desc='" + desc + '\'' +
                '}';
    }
}
