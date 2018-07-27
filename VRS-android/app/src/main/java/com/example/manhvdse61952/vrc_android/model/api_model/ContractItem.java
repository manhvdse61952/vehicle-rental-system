package com.example.manhvdse61952.vrc_android.model.api_model;

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
    private int ownerID;
    private int customerID;

    private int outsideFee;
    private int insideFee;
    private long endRealTime;
    private String issueContent;
    private Boolean isOwnerIssue;
    private Boolean isInsideIssue;
    private Boolean isOutsideIssue;
    private int penaltyOverTime;
    private long rentFeePerDay;
    private long rentFeePerHour;
    private float discountVehicle;
    private float discountGeneral;


    public ContractItem() {
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

    public long getRentFeePerDay() {
        return rentFeePerDay;
    }

    public void setRentFeePerDay(long rentFeePerDay) {
        this.rentFeePerDay = rentFeePerDay;
    }

    public long getRentFeePerHour() {
        return rentFeePerHour;
    }

    public void setRentFeePerHour(long rentFeePerHour) {
        this.rentFeePerHour = rentFeePerHour;
    }

    public String getIssueContent() {
        return issueContent;
    }

    public void setIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public int getOutsideFee() {
        return outsideFee;
    }

    public void setOutsideFee(int outsideFee) {
        this.outsideFee = outsideFee;
    }

    public int getInsideFee() {
        return insideFee;
    }

    public void setInsideFee(int insideFee) {
        this.insideFee = insideFee;
    }

    public long getEndRealTime() {
        return endRealTime;
    }

    public void setEndRealTime(long endRealTime) {
        this.endRealTime = endRealTime;
    }

    public Boolean getOwnerIssue() {
        return isOwnerIssue;
    }

    public void setOwnerIssue(Boolean ownerIssue) {
        isOwnerIssue = ownerIssue;
    }

    public Boolean getInsideIssue() {
        return isInsideIssue;
    }

    public void setInsideIssue(Boolean insideIssue) {
        isInsideIssue = insideIssue;
    }

    public Boolean getOutsideIssue() {
        return isOutsideIssue;
    }

    public void setOutsideIssue(Boolean outsideIssue) {
        isOutsideIssue = outsideIssue;
    }

    public int getPenaltyOverTime() {
        return penaltyOverTime;
    }

    public void setPenaltyOverTime(int penaltyOverTime) {
        this.penaltyOverTime = penaltyOverTime;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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
