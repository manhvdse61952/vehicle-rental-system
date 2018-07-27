package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class DiscountGeneral implements Serializable {
    private int id;
    private String code;
    private long start_time;
    private float value;
    private long end_time;
    private String status;
    private String discountType;

    public DiscountGeneral() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
}
