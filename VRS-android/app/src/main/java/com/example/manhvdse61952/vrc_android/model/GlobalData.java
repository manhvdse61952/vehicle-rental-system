package com.example.manhvdse61952.vrc_android.model;

import android.app.Application;

import com.example.manhvdse61952.vrc_android.model.apiModel.City;

import java.util.List;

public class GlobalData extends Application {

    private List<City> listCity;

    public List<City> getListCity() {
        return listCity;
    }

    public void setListCity(List<City> listCity) {
        this.listCity = listCity;
    }
}
