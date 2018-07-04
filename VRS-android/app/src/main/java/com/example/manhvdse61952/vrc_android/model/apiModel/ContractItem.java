package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.io.Serializable;

public class ContractItem implements Serializable {
    private String ContractID;
    private String contractStatus;
    private long startTime;
    private long endTime;
    private String ownerName;
    private String vehicleMaker;
    private String vehicleModel;
    private String vehicleYear;
    private String vehicleSeat;
    private String customerName;
    private int receiveType;
    private int rentDay;
    private int rentHour;
    private String depositFee;
    private String totalFee;
    private long serverTime;
    private String ownerPhone;
    private String customerCMND;
    private String customerPhone;

    public ContractItem(String contractID, String contractStatus, long startTime, long endTime, String ownerName, String vehicleMaker, String vehicleModel, String vehicleYear, String vehicleSeat, String customerName, int receiveType, int rentDay, int rentHour, String depositFee, String totalFee, long serverTime, String ownerPhone, String customerCMND, String customerPhone) {
        ContractID = contractID;
        this.contractStatus = contractStatus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ownerName = ownerName;
        this.vehicleMaker = vehicleMaker;
        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.vehicleSeat = vehicleSeat;
        this.customerName = customerName;
        this.receiveType = receiveType;
        this.rentDay = rentDay;
        this.rentHour = rentHour;
        this.depositFee = depositFee;
        this.totalFee = totalFee;
        this.serverTime = serverTime;
        this.ownerPhone = ownerPhone;
        this.customerCMND = customerCMND;
        this.customerPhone = customerPhone;
    }

    public ContractItem() {
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getCustomerCMND() {
        return customerCMND;
    }

    public void setCustomerCMND(String customerCMND) {
        this.customerCMND = customerCMND;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getContractID() {
        return ContractID;
    }

    public void setContractID(String contractID) {
        ContractID = contractID;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleSeat() {
        return vehicleSeat;
    }

    public void setVehicleSeat(String vehicleSeat) {
        this.vehicleSeat = vehicleSeat;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(int receiveType) {
        this.receiveType = receiveType;
    }

    public int getRentDay() {
        return rentDay;
    }

    public void setRentDay(int rentDay) {
        this.rentDay = rentDay;
    }

    public int getRentHour() {
        return rentHour;
    }

    public void setRentHour(int rentHour) {
        this.rentHour = rentHour;
    }

    public String getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(String depositFee) {
        this.depositFee = depositFee;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}
