package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class BusyDay implements Serializable {
    private Boolean busyMon;
    private Boolean busyTue;
    private Boolean busyWed;
    private Boolean busyThu;
    private Boolean busyFri;
    private Boolean busySat;
    private Boolean busySun;

    public BusyDay() {
    }

    public Boolean getBusyMon() {
        return busyMon;
    }

    public void setBusyMon(Boolean busyMon) {
        this.busyMon = busyMon;
    }

    public Boolean getBusyTue() {
        return busyTue;
    }

    public void setBusyTue(Boolean busyTue) {
        this.busyTue = busyTue;
    }

    public Boolean getBusyWed() {
        return busyWed;
    }

    public void setBusyWed(Boolean busyWed) {
        this.busyWed = busyWed;
    }

    public Boolean getBusyThu() {
        return busyThu;
    }

    public void setBusyThu(Boolean busyThu) {
        this.busyThu = busyThu;
    }

    public Boolean getBusyFri() {
        return busyFri;
    }

    public void setBusyFri(Boolean busyFri) {
        this.busyFri = busyFri;
    }

    public Boolean getBusySat() {
        return busySat;
    }

    public void setBusySat(Boolean busySat) {
        this.busySat = busySat;
    }

    public Boolean getBusySun() {
        return busySun;
    }

    public void setBusySun(Boolean busySun) {
        this.busySun = busySun;
    }
}
