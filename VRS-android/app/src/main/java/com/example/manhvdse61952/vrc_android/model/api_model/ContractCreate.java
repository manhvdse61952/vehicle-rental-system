package com.example.manhvdse61952.vrc_android.model.api_model;

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
    private float discountVehicle;
    private float discountGeneral;


    public ContractCreate() {
    }

    public float getDiscountVehicle() {
        return discountVehicle;
    }

    public void setDiscountVehicle(float discountVehicle) {
        this.discountVehicle = discountVehicle;
    }

    public float getDiscountGeneral() {
        return discountGeneral;
    }

    public void setDiscountGeneral(float discountGeneral) {
        this.discountGeneral = discountGeneral;
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
