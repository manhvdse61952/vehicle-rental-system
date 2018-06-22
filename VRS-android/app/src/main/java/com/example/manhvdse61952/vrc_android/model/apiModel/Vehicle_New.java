package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.io.Serializable;

public class Vehicle_New implements Serializable {
    private String frameNumber;
    private int ownerUserID;
    private int vehicleInformationID;
    private String description;
    private float rentFeePerSlot;
    private float rentFeePerDay;
    private float rentFeePerHours;
    private float depositFee;
    private String plateNumber;
    private int requireHouseHold;
    private int requireIdCard;
    private int discountID;
    private int districtID;
    private String currentStatus;
    private String approveStatus;
    private String imageLinkVehicleRegistration;
    private String imageLinkFront;
    private String imageLinkBack;

    public Vehicle_New(String frameNumber, int ownerUserID, int vehicleInformationID, String description, float rentFeePerSlot, float rentFeePerDay, float rentFeePerHours, float depositFee, String plateNumber, int requireHouseHold, int requireIdCard, int discountID, int districtID, String currentStatus, String approveStatus, String imageLinkVehicleRegistration, String imageLinkFront, String imageLinkBack) {
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
    }

    public Vehicle_New(String frameNumber, int ownerUserID, int vehicleInformationID, String description, float rentFeePerSlot, float rentFeePerDay, float rentFeePerHours, float depositFee, String plateNumber, int requireHouseHold, int requireIdCard, int districtID) {
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
        this.districtID = districtID;
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
}
