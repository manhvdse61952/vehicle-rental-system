package com.example.manhvdse61952.vrc_android.model.api_model;

import java.io.Serializable;

public class Feedback implements Serializable {
    private String username;
    private String content;
    private int rate;
    private long createTime;

    public Feedback() {
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
