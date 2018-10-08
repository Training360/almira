package com.training360.locationsapp.controller;

public class DeleteLocationResponse {

    private boolean success;

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
