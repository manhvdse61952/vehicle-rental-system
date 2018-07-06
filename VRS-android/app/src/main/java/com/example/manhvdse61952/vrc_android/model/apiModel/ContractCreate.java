package com.example.manhvdse61952.vrc_android.model.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ContractCreate implements Serializable {
    private String vehicleID;
    private int userID;
    private String paypalOrderID;
    private String paypalUserID;
    private int rentFeePerHourID;
    private int rentFeePerDayID;
    private long startTime;
    private long endTime;
    private float rentFee;
    private int hours;
    private int days;
    private int receiveType;

    public ContractCreate(String vehicleID, int userID, String paypalOrderID, String paypalUserID, int rentFeePerHourID, int rentFeePerDayID, long startTime, long endTime, float rentFee, int hours, int days, int receiveType) {
        this.vehicleID = vehicleID;
        this.userID = userID;
        this.paypalOrderID = paypalOrderID;
        this.paypalUserID = paypalUserID;
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

    public String getPaypalUserID() {
        return paypalUserID;
    }

    public void setPaypalUserID(String paypalUserID) {
        this.paypalUserID = paypalUserID;
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

    public int getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(int receiveType) {
        this.receiveType = receiveType;
    }
}
