package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class ContractFinish implements Serializable {
    private int customerID;
    private int ownerID;
    private String contractStatus;
    private long endRealTime;
    private String depositFee;
    private String rentFee;
    private String deliveryType;
    private long startTime;
    private long endTime;
    private long createAt;
    private String ownerPhone;
    private String ownerName;

    public ContractFinish(int customerID, int ownerID, String contractStatus, long endRealTime, String depositFee, String rentFee, String deliveryType, long startTime, long endTime, long createAt, String ownerPhone, String ownerName) {
        this.customerID = customerID;
        this.ownerID = ownerID;
        this.contractStatus = contractStatus;
        this.endRealTime = endRealTime;
        this.depositFee = depositFee;
        this.rentFee = rentFee;
        this.deliveryType = deliveryType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createAt = createAt;
        this.ownerPhone = ownerPhone;
        this.ownerName = ownerName;
    }

    public ContractFinish() {
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public long getEndRealTime() {
        return endRealTime;
    }

    public void setEndRealTime(long endRealTime) {
        this.endRealTime = endRealTime;
    }

    public String getDepositFee() {
        return depositFee;
    }

    public void setDepositFee(String depositFee) {
        this.depositFee = depositFee;
    }

    public String getRentFee() {
        return rentFee;
    }

    public void setRentFee(String rentFee) {
        this.rentFee = rentFee;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
