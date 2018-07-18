package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class VehicleUpdate implements Serializable {
    private float depositFee;
    private boolean requiredHouseHold;
    private boolean requiredID;
    private String address;
    private boolean isGasoline;
    private boolean isManual;
    private float rentFeeByHour;
    private float rentFeeByDay;
    private String description;

    public VehicleUpdate(float depositFee, boolean requiredHouseHold, boolean requiredID, String address, boolean isGasoline, boolean isManual, float rentFeeByHour, float rentFeeByDay, String description) {
        this.depositFee = depositFee;
        this.requiredHouseHold = requiredHouseHold;
        this.requiredID = requiredID;
        this.address = address;
        this.isGasoline = isGasoline;
        this.isManual = isManual;
        this.rentFeeByHour = rentFeeByHour;
        this.rentFeeByDay = rentFeeByDay;
        this.description = description;
    }

    public VehicleUpdate() {
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

    public boolean isGasoline() {
        return isGasoline;
    }

    public void setGasoline(boolean gasoline) {
        isGasoline = gasoline;
    }

    public boolean isManual() {
        return isManual;
    }

    public void setManual(boolean manual) {
        isManual = manual;
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
