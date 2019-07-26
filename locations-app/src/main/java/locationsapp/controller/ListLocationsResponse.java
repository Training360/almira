package locationsapp.controller;

import locationsapp.entities.Location;

import java.util.List;

public class ListLocationsResponse {

    private List<Location> locations;

    private int start;

    private int count;

    public ListLocationsResponse(List<Location> locations, int start, int count) {
        this.locations = locations;
        this.start = start;
        this.count = count;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
