package com.example.manhvdse61952.vrc_android.model.search_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchVehicleItem implements Serializable {
    @SerializedName("seat")
    @Expose
    private int seat;
    @SerializedName("frameNumber")
    @Expose
    private String frameNumber;
    @SerializedName("districtID")
    @Expose
    private int districtID;
    @SerializedName("rentFeePerHours")
    @Expose
    private String rentFeePerHours;
    @SerializedName("vehicleMaker")
    @Expose
    private String vehicleMaker;
    @SerializedName("currentStatus")
    @Expose
    private String currentStatus;
    @SerializedName("vehicleModel")
    @Expose
    private String vehicleModel;
    @SerializedName("imageLinkFront")
    @Expose
    private String imageLinkFront;
    @SerializedName("discountValue")
    @Expose
    private int discountValue;
    @SerializedName("rentFeePerDay")
    @Expose
    private String rentFeePerDay;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

    public SearchVehicleItem(int seat, String frameNumber, int districtID, String rentFeePerHours, String vehicleMaker, String currentStatus, String vehicleModel, String imageLinkFront, int discountValue, String rentFeePerDay, String vehicleType) {
        this.seat = seat;
        this.frameNumber = frameNumber;
        this.districtID = districtID;
        this.rentFeePerHours = rentFeePerHours;
        this.vehicleMaker = vehicleMaker;
        this.currentStatus = currentStatus;
        this.vehicleModel = vehicleModel;
        this.imageLinkFront = imageLinkFront;
        this.discountValue = discountValue;
        this.rentFeePerDay = rentFeePerDay;
        this.vehicleType = vehicleType;
    }

    public SearchVehicleItem() {
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public String getRentFeePerHours() {
        return rentFeePerHours;
    }

    public void setRentFeePerHours(String rentFeePerHours) {
        this.rentFeePerHours = rentFeePerHours;
    }

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
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

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public String getRentFeePerDay() {
        return rentFeePerDay;
    }

    public void setRentFeePerDay(String rentFeePerDay) {
        this.rentFeePerDay = rentFeePerDay;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
