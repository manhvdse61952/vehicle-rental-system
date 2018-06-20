package com.example.manhvdse61952.vrc_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address{
    @SerializedName("districtId")
    @Expose
    private int districtId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;


    public Address(int districtId, String city, String district) {
        this.districtId = districtId;
        this.city = city;
        this.district = district;
    }

    public Address(){

    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
