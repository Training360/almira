package com.training360.locationsapp.ws;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace = "http://training360.com/locations")
public class CreateLocationResponse {

    private LocationDto location;

    public CreateLocationResponse() {
    }

    public CreateLocationResponse(LocationDto location) {
        this.location = location;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }
}
