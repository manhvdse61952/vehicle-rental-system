package com.example.manhvdse61952.vrc_android.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String usernameOrEmail;
    private String password;

    public Account(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
