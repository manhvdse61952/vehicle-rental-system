package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.io.Serializable;

public class VehicleInformation_New implements Serializable {
    private int vehicleInformationID;
    private String vehicleMaker;
    private int seat;
    private int modelYear;
    private String vehicleType;
    private String vehicleModel;

    public VehicleInformation_New(int vehicleInformationID, String vehicleMaker, int seat, int modelYear, String vehicleType, String vehicleModel) {
        this.vehicleInformationID = vehicleInformationID;
        this.vehicleMaker = vehicleMaker;
        this.seat = seat;
        this.modelYear = modelYear;
        this.vehicleType = vehicleType;
        this.vehicleModel = vehicleModel;
    }

    public VehicleInformation_New(){

    }

    public int getVehicleInformationID() {
        return vehicleInformationID;
    }

    public void setVehicleInformationID(int vehicleInformationID) {
        this.vehicleInformationID = vehicleInformationID;
    }

    public String getVehicleMaker() {
        return vehicleMaker;
    }

    public void setVehicleMaker(String vehicleMaker) {
        this.vehicleMaker = vehicleMaker;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }
}
