package com.example.manhvdse61952.vrc_android.model.search_model;

import java.io.Serializable;

public class DetailVehicleItem implements Serializable {
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
    private double deposit;
    private int rentFeePerHourID;
    private int rentFeePerDayID;
    private int seat;
    private String vehicleType;
    private String address;
    private String description;

    public int getRentFeePerHourID() {
        return rentFeePerHourID;
    }

    public void setRentFeePerHourID(int rentFeePerHourID) {
        this.rentFeePerHourID = rentFeePerHourID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getRentFeePerDayID() {
        return rentFeePerDayID;
    }

    public void setRentFeePerDayID(int rentFeePerDayID) {
        this.rentFeePerDayID = rentFeePerDayID;
    }

    public DetailVehicleItem() {
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
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
