package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class Tracking implements Serializable {
    private String vehicleFrameNumber;
    private double longitude;
    private double latitude;

    public Tracking(String vehicleFrameNumber, double longitude, double latitude) {
        this.vehicleFrameNumber = vehicleFrameNumber;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Tracking() {
    }

    public String getVehicleFrameNumber() {
        return vehicleFrameNumber;
    }

    public void setVehicleFrameNumber(String vehicleFrameNumber) {
        this.vehicleFrameNumber = vehicleFrameNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
