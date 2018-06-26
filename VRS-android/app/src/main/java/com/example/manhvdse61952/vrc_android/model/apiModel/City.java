package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("city_name")
    @Expose
    public String cityName;
    @SerializedName("districtList")
    @Expose


    public List<District> districtList;

    public City(Integer id, String cityName, List<District> district) {
        this.id = id;
        this.cityName = cityName;
        this.districtList = district;
    }

    public City() {
    }


    @Override
    public String toString() {
        return cityName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<District> getDistrict() {
        return districtList;
    }

    public void setDistrict(List<District> district) {
        this.districtList = district;
    }
}

