package com.example.manhvdse61952.vrc_android.model;

import java.io.Serializable;

public class SearchItemNew implements Serializable {
    private String vehicleName;
    private int vehicleSeat;
    private String address;
    private String vehiclePrice;
    private String imageLinkFront;
    private String frameNumber;

    public SearchItemNew(String vehicleName, int vehicleSeat, String address, String vehiclePrice, String imageLinkFront, String frameNumber) {
        this.vehicleName = vehicleName;
        this.vehicleSeat = vehicleSeat;
        this.address = address;
        this.vehiclePrice = vehiclePrice;
        this.imageLinkFront = imageLinkFront;
        this.frameNumber = frameNumber;
    }

    public SearchItemNew(){

    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public int getVehicleSeat() {
        return vehicleSeat;
    }

    public void setVehicleSeat(int vehicleSeat) {
        this.vehicleSeat = vehicleSeat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVehiclePrice() {
        return vehiclePrice;
    }

    public void setVehiclePrice(String vehiclePrice) {
        this.vehiclePrice = vehiclePrice;
    }

    public String getImageLinkFront() {
        return imageLinkFront;
    }

    public void setImageLinkFront(String imageLinkFront) {
        this.imageLinkFront = imageLinkFront;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }
}
