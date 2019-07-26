package com.training360.locationsapp.ws;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "lat", "lon"})
public class UpdateLocationRequest {

    private long id;

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private double lat;

    @XmlAttribute(required = true)
    private double lon;

}
