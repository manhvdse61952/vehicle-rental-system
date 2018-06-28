package com.example.manhvdse61952.vrc_android.model.searchModel;

import java.io.Serializable;

public class MainItemModel implements Serializable {
    private Boolean requireIdCard;
    private String ownerFullName;
    private String vehicleMaker;
    private Boolean isManual;
    private double rentFeePerDay;
    private String imageLinkBack;
    private int ownerID;
    private int modelYear;
    private Boolean isGasoline;
    private int districtID;
    private String vehicleModel;
    private String imageLinkFront;
    private double rentFeePerSlot;
    private Boolean requireHouseHold;
    private double rentFeePerHour;
    private String plateNumber;
    private Boolean isScooter;

    public MainItemModel(Boolean requireIdCard, String ownerFullName, String vehicleMaker, Boolean isManual, double rentFeePerDay, String imageLinkBack, int ownerID, int modelYear, Boolean isGasoline, int districtID, String vehicleModel, String imageLinkFront, double rentFeePerSlot, Boolean requireHouseHold, double rentFeePerHour, String plateNumber, Boolean isScooter) {
        this.requireIdCard = requireIdCard;
        this.ownerFullName = ownerFullName;
        this.vehicleMaker = vehicleMaker;
        this.isManual = isManual;
        this.rentFeePerDay = rentFeePerDay;
        this.imageLinkBack = imageLinkBack;
        this.ownerID = ownerID;
        this.modelYear = modelYear;
        this.isGasoline = isGasoline;
        this.districtID = districtID;
        this.vehicleModel = vehicleModel;
        this.imageLinkFront = imageLinkFront;
        this.rentFeePerSlot = rentFeePerSlot;
        this.requireHouseHold = requireHouseHold;
        this.rentFeePerHour = rentFeePerHour;
        this.plateNumber = plateNumber;
        this.isScooter = isScooter;
    }

    public MainItemModel() {
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Boolean getScooter() {
        return isScooter;
    }

    public void setScooter(Boolean scooter) {
        isScooter = scooter;
    }

    public Boolean getRequireIdCard() {
        return requireIdCard;
    }

    public void setRequireIdCard(Boolean requireIdCard) {
        this.requireIdCard = requireIdCard;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public Boolean getManual() {
        return isManual;
    }

    public void setManual(Boolean manual) {
        isManual = manual;
    }

    public double getRentFeePerDay() {
        return rentFeePerDay;
    }

    public void setRentFeePerDay(double rentFeePerDay) {
        this.rentFeePerDay = rentFeePerDay;
    }

    public String getImageLinkBack() {
        return imageLinkBack;
    }

    public void setImageLinkBack(String imageLinkBack) {
        this.imageLinkBack = imageLinkBack;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public Boolean getGasoline() {
        return isGasoline;
    }

    public void setGasoline(Boolean gasoline) {
        isGasoline = gasoline;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getImageLinkFront() {
        return imageLinkFront;
    }

    public void setImageLinkFront(String imageLinkFront) {
        this.imageLinkFront = imageLinkFront;
    }

    public double getRentFeePerSlot() {
        return rentFeePerSlot;
    }

    public void setRentFeePerSlot(double rentFeePerSlot) {
        this.rentFeePerSlot = rentFeePerSlot;
    }

    public Boolean getRequireHouseHold() {
        return requireHouseHold;
    }

    public void setRequireHouseHold(Boolean requireHouseHold) {
        this.requireHouseHold = requireHouseHold;
    }

    public double getRentFeePerHour() {
        return rentFeePerHour;
    }

    public void setRentFeePerHour(double rentFeePerHour) {
        this.rentFeePerHour = rentFeePerHour;
    }
}