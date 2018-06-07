package com.example.manhvdse61952.vrc_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchItem {
    @SerializedName("img_vehicle")
    @Expose
    private String img_vehicle;

    @SerializedName("vehicle_name")
    @Expose
    private String vehicle_name;

    @SerializedName("seat")
    @Expose
    private int seat;

    @SerializedName("rent_price")
    @Expose
    private String rent_price;

    @SerializedName("vehicle_description")
    @Expose
    private String vehicle_description;


    public String getImg_vehicle() {
        return img_vehicle;
    }

    public void setImg_vehicle(String img_vehicle) {
        this.img_vehicle = img_vehicle;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getRent_price() {
        return rent_price;
    }

    public void setRent_price(String rent_price) {
        this.rent_price = rent_price;
    }

    public String getVehicle_description() {
        return vehicle_description;
    }

    public void setVehicle_description(String vehicle_description) {
        this.vehicle_description = vehicle_description;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }
}
