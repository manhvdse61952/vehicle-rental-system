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
    private String vehicleDeliveryType;
    private Boolean busyMon;
    private Boolean busyTue;
    private Boolean busyWed;
    private Boolean busyThu;
    private Boolean busyFri;
    private Boolean busySat;
    private Boolean busySun;


    public VehicleUpdate() {
    }

    public Boolean getBusyMon() {
        return busyMon;
    }

    public void setBusyMon(Boolean busyMon) {
        this.busyMon = busyMon;
    }

    public Boolean getBusyTue() {
        return busyTue;
    }

    public void setBusyTue(Boolean busyTue) {
        this.busyTue = busyTue;
    }

    public Boolean getBusyWed() {
        return busyWed;
    }

    public void setBusyWed(Boolean busyWed) {
        this.busyWed = busyWed;
    }

    public Boolean getBusyThu() {
        return busyThu;
    }

    public void setBusyThu(Boolean busyThu) {
        this.busyThu = busyThu;
    }

    public Boolean getBusyFri() {
        return busyFri;
    }

    public void setBusyFri(Boolean busyFri) {
        this.busyFri = busyFri;
    }

    public Boolean getBusySat() {
        return busySat;
    }

    public void setBusySat(Boolean busySat) {
        this.busySat = busySat;
    }

    public Boolean getBusySun() {
        return busySun;
    }

    public void setBusySun(Boolean busySun) {
        this.busySun = busySun;
    }

    public String getVehicleDeliveryType() {
        return vehicleDeliveryType;
    }

    public void setVehicleDeliveryType(String vehicleDeliveryType) {
        this.vehicleDeliveryType = vehicleDeliveryType;
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
