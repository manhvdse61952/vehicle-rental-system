package com.example.manhvdse61952.vrc_android.model.apiModel;

public class VehicleInformation {
    private String vehicleName;
    private int seat;
    private int modelYear;
    private String engineType;
    private String tranmission;

    public VehicleInformation(String vehicleName, int seat, int modelYear, String engineType, String tranmission) {
        this.vehicleName = vehicleName;
        this.seat = seat;
        this.modelYear = modelYear;
        this.engineType = engineType;
        this.tranmission = tranmission;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
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

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getTranmission() {
        return tranmission;
    }

    public void setTranmission(String tranmission) {
        this.tranmission = tranmission;
    }

    public VehicleInformation() {

    }
}
