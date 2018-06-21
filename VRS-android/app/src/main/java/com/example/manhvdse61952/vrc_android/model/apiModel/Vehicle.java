package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private String frameNumber;
    private double rentFeePerSlot;
    private double depositFee;
    private VehicleInformation vehicleInformation;
    public int ownerID;

    public Vehicle() {
    }

    public Vehicle(String frameNumber, double rentFee, double depositFee, VehicleInformation vehicleInformation, int ownerID) {
        this.frameNumber = frameNumber;
        this.rentFeePerSlot = rentFee;
        this.depositFee = depositFee;
        this.vehicleInformation = vehicleInformation;
        this.ownerID = ownerID;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public double getRentFee() {
        return rentFeePerSlot;
    }

    public void setRentFee(double rentFee) {
        this.rentFeePerSlot = rentFee;
    }

    public double getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(double depositFee) {
        this.depositFee = depositFee;
    }

    public VehicleInformation getVehicleInformation() {
        return vehicleInformation;
    }

    public void setVehicleInformation(VehicleInformation vehicleInformation) {
        this.vehicleInformation = vehicleInformation;
    }
}
