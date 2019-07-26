package locationsapp.controller;

public class CreateLocationRequest {

    private String name;

    private String coords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    @Override
    public String toString() {
        return "CreateLocationRequest{" +
                "name='" + name + '\'' +
                ", coords='" + coords + '\'' +
                '}';
    }
}
