package com.example.manhvdse61952.vrc_android.model.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContractCreate implements Serializable {
    @SerializedName("vehicleID")
    @Expose
    private String vehicleID;
    @SerializedName("userID")
    @Expose
    private int userID;
    @SerializedName("paypalOrderID")
    @Expose
    private String paypalOrderID;
    @SerializedName("paypalOwnerID")
    @Expose
    private String paypalOwnerID;
    @SerializedName("rentFeePerHourID")
    @Expose
    private int rentFeePerHourID;
    @SerializedName("rentFeePerDayID")
    @Expose
    private int rentFeePerDayID;

    @SerializedName("startTime")
    @Expose
    private long startTime;

    @SerializedName("endTime")
    @Expose
    private long endTime;

    @SerializedName("rentFee")
    @Expose
    private float rentFee;

    @SerializedName("hours")
    @Expose
    private int hours;
    @SerializedName("days")
    @Expose
    private int days;
    @SerializedName("receiveType")
    @Expose
    private int receiveType;

    public ContractCreate(String vehicleID, int userID, String paypalOrderID, String paypalOwnerID, int rentFeePerHourID, int rentFeePerDayID, long startTime, long endTime, float rentFee, int hours, int days, int receiveType) {
        this.vehicleID = vehicleID;
        this.userID = userID;
        this.paypalOrderID = paypalOrderID;
        this.paypalOwnerID = paypalOwnerID;
        this.rentFeePerHourID = rentFeePerHourID;
        this.rentFeePerDayID = rentFeePerDayID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rentFee = rentFee;
        this.hours = hours;
        this.days = days;
        this.receiveType = receiveType;
    }

    public ContractCreate() {
    }

    public int getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(int receiveType) {
        this.receiveType = receiveType;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPaypalOrderID() {
        return paypalOrderID;
    }

    public void setPaypalOrderID(String paypalOrderID) {
        this.paypalOrderID = paypalOrderID;
    }

    public String getPaypalOwnerID() {
        return paypalOwnerID;
    }

    public void setPaypalOwnerID(String paypalOwnerID) {
        this.paypalOwnerID = paypalOwnerID;
    }

    public int getRentFeePerHourID() {
        return rentFeePerHourID;
    }

    public void setRentFeePerHourID(int rentFeePerHourID) {
        this.rentFeePerHourID = rentFeePerHourID;
    }

    public int getRentFeePerDayID() {
        return rentFeePerDayID;
    }

    public void setRentFeePerDayID(int rentFeePerDayID) {
        this.rentFeePerDayID = rentFeePerDayID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public float getRentFee() {
        return rentFee;
    }

    public void setRentFee(float rentFee) {
        this.rentFee = rentFee;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
