package com.training360.locationsapp.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(namespace = "http://training360.com/locations")
public class ListLocationsResponse {

    private List<LocationDto> locations;

    public ListLocationsResponse() {
    }

    public ListLocationsResponse(List<LocationDto> locations) {
        this.locations = locations;
    }

    public List<LocationDto> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDto> locations) {
        this.locations = locations;
    }
}
