package com.example.manhvdse61952.vrc_android.model;

import java.io.Serializable;

public class Vehicle_New implements Serializable {
    private String frameNumber;
    private int ownerUserID;
    private int vehicleInformationID;
    private String description;
    private String rentFeePerSlot;
    private String rentFeePerDay;
    private String rentFeePerHours;
    private String depositFee;
    private String plateNumber;
    private String requireHouseHold;
    private String requireIdCard;
    private int discountID;
    private int districtID;
    private String currentStatus;
    private String approveStatus;
    private String imageLinkVehicleRegistration;
    private String imageLinkFront;
    private String imageLinkBack;
    private String address;

    public Vehicle_New(String frameNumber, int ownerUserID, int vehicleInformationID, String description, String rentFeePerSlot, String rentFeePerDay, String rentFeePerHours, String depositFee, String plateNumber, String requireHouseHold, String requireIdCard, int discountID, int districtID, String currentStatus, String approveStatus, String imageLinkVehicleRegistration, String imageLinkFront, String imageLinkBack, String address) {
        this.frameNumber = frameNumber;
        this.ownerUserID = ownerUserID;
        this.vehicleInformationID = vehicleInformationID;
        this.description = description;
        this.rentFeePerSlot = rentFeePerSlot;
        this.rentFeePerDay = rentFeePerDay;
        this.rentFeePerHours = rentFeePerHours;
        this.depositFee = depositFee;
        this.plateNumber = plateNumber;
        this.requireHouseHold = requireHouseHold;
        this.requireIdCard = requireIdCard;
        this.discountID = discountID;
        this.districtID = districtID;
        this.currentStatus = currentStatus;
        this.approveStatus = approveStatus;
        this.imageLinkVehicleRegistration = imageLinkVehicleRegistration;
        this.imageLinkFront = imageLinkFront;
        this.imageLinkBack = imageLinkBack;
        this.address = address;
    }

    public Vehicle_New(){

    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getOwnerUserID() {
        return ownerUserID;
    }

    public void setOwnerUserID(int ownerUserID) {
        this.ownerUserID = ownerUserID;
    }

    public int getVehicleInformationID() {
        return vehicleInformationID;
    }

    public void setVehicleInformationID(int vehicleInformationID) {
        this.vehicleInformationID = vehicleInformationID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRentFeePerSlot() {
        return rentFeePerSlot;
    }

    public void setRentFeePerSlot(String rentFeePerSlot) {
        this.rentFeePerSlot = rentFeePerSlot;
    }

    public String getRentFeePerDay() {
        return rentFeePerDay;
    }

    public void setRentFeePerDay(String rentFeePerDay) {
        this.rentFeePerDay = rentFeePerDay;
    }

    public String getRentFeePerHours() {
        return rentFeePerHours;
    }

    public void setRentFeePerHours(String rentFeePerHours) {
        this.rentFeePerHours = rentFeePerHours;
    }

    public String getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(String depositFee) {
        this.depositFee = depositFee;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getRequireHouseHold() {
        return requireHouseHold;
    }

    public void setRequireHouseHold(String requireHouseHold) {
        this.requireHouseHold = requireHouseHold;
    }

    public String getRequireIdCard() {
        return requireIdCard;
    }

    public void setRequireIdCard(String requireIdCard) {
        this.requireIdCard = requireIdCard;
    }

    public int getDiscountID() {
        return discountID;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getImageLinkVehicleRegistration() {
        return imageLinkVehicleRegistration;
    }

    public void setImageLinkVehicleRegistration(String imageLinkVehicleRegistration) {
        this.imageLinkVehicleRegistration = imageLinkVehicleRegistration;
    }

    public String getImageLinkFront() {
        return imageLinkFront;
    }

    public void setImageLinkFront(String imageLinkFront) {
        this.imageLinkFront = imageLinkFront;
    }

    public String getImageLinkBack() {
        return imageLinkBack;
    }

    public void setImageLinkBack(String imageLinkBack) {
        this.imageLinkBack = imageLinkBack;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
