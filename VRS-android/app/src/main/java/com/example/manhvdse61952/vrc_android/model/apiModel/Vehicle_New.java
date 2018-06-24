package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.io.Serializable;

public class Vehicle_New implements Serializable {
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

    public Vehicle_New(String frameNumber, int ownerID, int vehicleInformationID, String description, float rentFeePerSlot, float rentFeePerDay, float rentFeePerHours, float depositFee, String plateNumber, int requireHouseHold, int requireIdCard, int districtID) {
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
    }

    public Vehicle_New(){

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
