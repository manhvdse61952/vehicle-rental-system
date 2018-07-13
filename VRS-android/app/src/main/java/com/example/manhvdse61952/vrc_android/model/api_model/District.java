package com.example.manhvdse61952.vrc_android.model.api_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class District {

   @SerializedName("id")
   @Expose
   public Integer id;
   @SerializedName("district_name")
   @Expose
   public String districtName;

    public District() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Override
    public String toString() {
        return districtName;
    }
}
