package com.example.manhvdse61952.vrc_android.model.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {
    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    @SerializedName("tokenType")
    @Expose
    private String tokenType;

    @SerializedName("expirationDateTime")
    @Expose
    private String expirationDateTime;

    @SerializedName("userID")
    @Expose
    private int userID;

    @SerializedName("roleName")
    @Expose
    private String roleName;

    @SerializedName("fullname")
    @Expose
    private String fullname;

    public Account(String accessToken, String tokenType, String expirationDateTime, int userID, String roleName, String fullname) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expirationDateTime = expirationDateTime;
        this.userID = userID;
        this.roleName = roleName;
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Account() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getExpirationDateTime() {
        return expirationDateTime;
    }

    public void setExpirationDateTime(String expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
