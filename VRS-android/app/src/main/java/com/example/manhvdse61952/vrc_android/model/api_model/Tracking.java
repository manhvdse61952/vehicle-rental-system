package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class Tracking implements Serializable {
    private double longitude;
    private double latitude;

    public Tracking() {
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
