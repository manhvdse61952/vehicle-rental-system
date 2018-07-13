package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class RentTime implements Serializable {
    private long startTime;
    private long endTime;

    public RentTime(long endTime, long startTime) {
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public RentTime() {
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
