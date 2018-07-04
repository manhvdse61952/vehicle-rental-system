package com.example.manhvdse61952.vrc_android.model.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehicleInformation implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("vehicleMaker")
    @Expose
    private String vehicleMaker;
    @SerializedName("seat")
    @Expose
    private int seat;
    @SerializedName("modelYear")
    @Expose
    private int modelYear;
    @SerializedName("vehicleModel")
    @Expose
    private String vehicleModel;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;
    @SerializedName("scooter")
    @Expose
    private Boolean scooter;


    public VehicleInformation(int id, String vehicleMaker, int seat, int modelYear, String vehicleModel, String vehicleType) {
        this.id = id;
        this.vehicleMaker = vehicleMaker;
        this.seat = seat;
        this.modelYear = modelYear;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.scooter = scooter;
    }

    public Boolean getScooter() {
        return scooter;
    }

    public void setScooter(Boolean scooter) {
        this.scooter = scooter;
    }

    public VehicleInformation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
