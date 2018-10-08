package com.training360.locationsapp.ws;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(namespace = "http://training360.com/locations")
public class DeleteLocationResponse {

    private boolean success;

    public DeleteLocationResponse() {
    }

    public DeleteLocationResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
