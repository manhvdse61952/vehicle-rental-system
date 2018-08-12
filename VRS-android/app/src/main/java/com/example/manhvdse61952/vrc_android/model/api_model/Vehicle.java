package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String frameNumber;
    private int ownerID;
    private int vehicleInformationID;
    private String description;
    private float rentFeePerSlot;
    private float rentFeePerDay;
    private float rentFeePerHours;
    private float depositFee;
    private String plateNumber;
    private int requireHouseHold;
    private int requireIdCard;
    private int districtID;
    private int isGasoline;
    private int isManual;
    private double longitude;
    private double latitude;
    private Boolean haveDriver;
    private int vehicleDeliveryType;
    private Boolean busyMon;
    private Boolean busyTue;
    private Boolean busyWed;
    private Boolean busyThu;
    private Boolean busyFri;
    private Boolean busySat;
    private Boolean busySun;

    public Vehicle(String frameNumber, int ownerID, int vehicleInformationID, String description, float rentFeePerSlot, float rentFeePerDay, float rentFeePerHours, float depositFee, String plateNumber, int requireHouseHold, int requireIdCard, int districtID, int isGasoline, int isManual, double longitude, double latitude, Boolean haveDriver, int vehicleDeliveryType, Boolean busyMon, Boolean busyTue, Boolean busyWed, Boolean busyThu, Boolean busyFri, Boolean busySat, Boolean busySun) {
        this.frameNumber = frameNumber;
        this.ownerID = ownerID;
        this.vehicleInformationID = vehicleInformationID;
        this.description = description;
        this.rentFeePerSlot = rentFeePerSlot;
        this.rentFeePerDay = rentFeePerDay;
        this.rentFeePerHours = rentFeePerHours;
        this.depositFee = depositFee;
        this.plateNumber = plateNumber;
        this.requireHouseHold = requireHouseHold;
        this.requireIdCard = requireIdCard;
        this.districtID = districtID;
        this.isGasoline = isGasoline;
        this.isManual = isManual;
        this.longitude = longitude;
        this.latitude = latitude;
        this.haveDriver = haveDriver;
        this.vehicleDeliveryType = vehicleDeliveryType;
        this.busyMon = busyMon;
        this.busyTue = busyTue;
        this.busyWed = busyWed;
        this.busyThu = busyThu;
        this.busyFri = busyFri;
        this.busySat = busySat;
        this.busySun = busySun;
    }

    public Vehicle(){

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

    public Boolean getHaveDriver() {
        return haveDriver;
    }

    public void setHaveDriver(Boolean haveDriver) {
        this.haveDriver = haveDriver;
    }

    public int getVehicleDeliveryType() {
        return vehicleDeliveryType;
    }

    public void setVehicleDeliveryType(int vehicleDeliveryType) {
        this.vehicleDeliveryType = vehicleDeliveryType;
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

    public int getIsGasoline() {
        return isGasoline;
    }

    public void setIsGasoline(int isGasoline) {
        this.isGasoline = isGasoline;
    }

    public int getIsManual() {
        return isManual;
    }

    public void setIsManual(int isManual) {
        this.isManual = isManual;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getVehicleInformationID() {
        return vehicleInformationID;
    }

    public void setVehicleInformationID(int vehicleInformationID) {
        this.vehicleInformationID = vehicleInformationID;
    }

    public float getRentFeePerSlot() {
        return rentFeePerSlot;
    }

    public void setRentFeePerSlot(float rentFeePerSlot) {
        this.rentFeePerSlot = rentFeePerSlot;
    }

    public float getRentFeePerDay() {
        return rentFeePerDay;
    }

    public void setRentFeePerDay(float rentFeePerDay) {
        this.rentFeePerDay = rentFeePerDay;
    }

    public float getRentFeePerHours() {
        return rentFeePerHours;
    }

    public void setRentFeePerHours(float rentFeePerHours) {
        this.rentFeePerHours = rentFeePerHours;
    }

    public float getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(float depositFee) {
        this.depositFee = depositFee;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getRequireHouseHold() {
        return requireHouseHold;
    }

    public void setRequireHouseHold(int requireHouseHold) {
        this.requireHouseHold = requireHouseHold;
    }

    public int getRequireIdCard() {
        return requireIdCard;
    }

    public void setRequireIdCard(int requireIdCard) {
        this.requireIdCard = requireIdCard;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }
}
