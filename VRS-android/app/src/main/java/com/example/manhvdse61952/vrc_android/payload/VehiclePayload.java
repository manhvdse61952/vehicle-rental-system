package com.example.manhvdse61952.vrc_android.payload;

import java.io.Serializable;
import java.util.Date;

public class VehiclePayload implements Serializable{
    private String frameNumber;
    private Integer ownerID;

    private Date registrationDeadLine;
    private float rentFee;
    private float depositFee;
    private String plateNumber;

    private String vehicleName;
    private Integer seat;
    private Integer modelYear;
    private String engineType;
    private String tranmission;

    public VehiclePayload() {
    }

    public VehiclePayload(String frameNumber, Integer ownerID, Date registrationDeadLine, float rentFee, float depositFee, String plateNumber, String vehicleName, Integer seat, Integer modelYear, String engineType, String tranmission) {
        this.frameNumber = frameNumber;
        this.ownerID = ownerID;
        this.registrationDeadLine = registrationDeadLine;
        this.rentFee = rentFee;
        this.depositFee = depositFee;
        this.plateNumber = plateNumber;
        this.vehicleName = vehicleName;
        this.seat = seat;
        this.modelYear = modelYear;
        this.engineType = engineType;
        this.tranmission = tranmission;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Date getRegistrationDeadLine() {
        return registrationDeadLine;
    }

    public void setRegistrationDeadLine(Date registrationDeadLine) {
        this.registrationDeadLine = registrationDeadLine;
    }

    public float getRentFee() {
        return rentFee;
    }

    public void setRentFee(float rentFee) {
        this.rentFee = rentFee;
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

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
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
}
