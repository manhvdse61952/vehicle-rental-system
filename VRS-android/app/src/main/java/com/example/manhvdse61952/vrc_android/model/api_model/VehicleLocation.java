package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class VehicleLocation implements Serializable {
    private String currentStatus;
    private float discountValue;
    private int districtID;
    private String frameNumber;
    private String imageLinkFront;
    private double latitude;
    private double longitude;
    private int rentFeePerDay;
    private int rentFeePerHours;
    private int seat;
    private String vehicleFrameNumber;
    private String vehicleMaker;
    private String vehicleModel;
    private String vehicleType;


    public VehicleLocation() {
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public float getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(float discountValue) {
        this.discountValue = discountValue;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getImageLinkFront() {
        return imageLinkFront;
    }

    public void setImageLinkFront(String imageLinkFront) {
        this.imageLinkFront = imageLinkFront;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRentFeePerDay() {
        return rentFeePerDay;
    }

    public void setRentFeePerDay(int rentFeePerDay) {
        this.rentFeePerDay = rentFeePerDay;
    }

    public int getRentFeePerHours() {
        return rentFeePerHours;
    }

    public void setRentFeePerHours(int rentFeePerHours) {
        this.rentFeePerHours = rentFeePerHours;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getVehicleFrameNumber() {
        return vehicleFrameNumber;
    }

    public void setVehicleFrameNumber(String vehicleFrameNumber) {
        this.vehicleFrameNumber = vehicleFrameNumber;
    }

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
