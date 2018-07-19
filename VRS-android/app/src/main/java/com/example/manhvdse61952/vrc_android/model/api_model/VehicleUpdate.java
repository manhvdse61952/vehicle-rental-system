package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class VehicleUpdate implements Serializable {
    private float depositFee;
    private boolean requiredHouseHold;
    private boolean requiredID;
    private String address;
    private float rentFeeByHour;
    private float rentFeeByDay;
    private String description;
    private String longitude;
    private String latitude;


    public VehicleUpdate() {
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public float getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(float depositFee) {
        this.depositFee = depositFee;
    }

    public boolean isRequiredHouseHold() {
        return requiredHouseHold;
    }

    public void setRequiredHouseHold(boolean requiredHouseHold) {
        this.requiredHouseHold = requiredHouseHold;
    }

    public boolean isRequiredID() {
        return requiredID;
    }

    public void setRequiredID(boolean requiredID) {
        this.requiredID = requiredID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRentFeeByHour() {
        return rentFeeByHour;
    }

    public void setRentFeeByHour(float rentFeeByHour) {
        this.rentFeeByHour = rentFeeByHour;
    }

    public float getRentFeeByDay() {
        return rentFeeByDay;
    }

    public void setRentFeeByDay(float rentFeeByDay) {
        this.rentFeeByDay = rentFeeByDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
