package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class Report implements Serializable {
    private int numberOfContract;
    private int numberOfFinishedContract;
    private int numberOfRefundedContract;
    private int numberOfInactiveContract;
    private int numberOfActiveContract;
    private int numberOfIssueContract;
    private int numberOfPendingContract;
    private int numberOfPreFinishContract;
    private double totalRentFee;
    private double totalOtherFee;
    private double totalRefunded;

    public int getNumberOfContract() {
        return numberOfContract;
    }

    public void setNumberOfContract(int numberOfContract) {
        this.numberOfContract = numberOfContract;
    }

    public int getNumberOfFinishedContract() {
        return numberOfFinishedContract;
    }

    public void setNumberOfFinishedContract(int numberOfFinishedContract) {
        this.numberOfFinishedContract = numberOfFinishedContract;
    }

    public int getNumberOfRefundedContract() {
        return numberOfRefundedContract;
    }

    public void setNumberOfRefundedContract(int numberOfRefundedContract) {
        this.numberOfRefundedContract = numberOfRefundedContract;
    }

    public int getNumberOfInactiveContract() {
        return numberOfInactiveContract;
    }

    public void setNumberOfInactiveContract(int numberOfInactiveContract) {
        this.numberOfInactiveContract = numberOfInactiveContract;
    }

    public int getNumberOfActiveContract() {
        return numberOfActiveContract;
    }

    public void setNumberOfActiveContract(int numberOfActiveContract) {
        this.numberOfActiveContract = numberOfActiveContract;
    }

    public int getNumberOfIssueContract() {
        return numberOfIssueContract;
    }

    public void setNumberOfIssueContract(int numberOfIssueContract) {
        this.numberOfIssueContract = numberOfIssueContract;
    }

    public int getNumberOfPendingContract() {
        return numberOfPendingContract;
    }

    public void setNumberOfPendingContract(int numberOfPendingContract) {
        this.numberOfPendingContract = numberOfPendingContract;
    }

    public int getNumberOfPreFinishContract() {
        return numberOfPreFinishContract;
    }

    public void setNumberOfPreFinishContract(int numberOfPreFinishContract) {
        this.numberOfPreFinishContract = numberOfPreFinishContract;
    }

    public double getTotalRentFee() {
        return totalRentFee;
    }

    public void setTotalRentFee(double totalRentFee) {
        this.totalRentFee = totalRentFee;
    }

    public double getTotalOtherFee() {
        return totalOtherFee;
    }

    public void setTotalOtherFee(double totalOtherFee) {
        this.totalOtherFee = totalOtherFee;
    }

    public double getTotalRefunded() {
        return totalRefunded;
    }

    public void setTotalRefunded(double totalRefunded) {
        this.totalRefunded = totalRefunded;
    }
}
