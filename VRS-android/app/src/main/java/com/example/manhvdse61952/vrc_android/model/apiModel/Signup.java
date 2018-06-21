package com.example.manhvdse61952.vrc_android.model.apiModel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class Signup implements Serializable {
    private String name;
    private String username;
    private String email;
    private String password;
    private String rolename;
    private String phone;

    private String address;

    private String idCard;
    private String imageLink;


    private String paypalID;


    public Signup(String address, String email, String idCard, String imageLink, String name, String password, String paypalID, String phone, String rolename, String username) {
        this.address = address;
        this.email = email;
        this.idCard = idCard;
        this.imageLink = imageLink;
        this.name = name;
        this.password = password;
        this.paypalID = paypalID;
        this.phone = phone;
        this.rolename = rolename;
        this.username = username;
    }

    public Signup() {

    }

    public Signup(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPaypalID() {
        return paypalID;
    }

    public void setPaypalID(String paypalID) {
        this.paypalID = paypalID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
