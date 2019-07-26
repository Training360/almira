package com.training360.locationsapp.ws;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"name", "lat", "lon"})
public class CreateLocationRequest {

    @XmlElement(required = true)
    private String name;

    @XmlElement(required = true)
    private double lat;

    @XmlElement(required = true)
    private double lon;

}
