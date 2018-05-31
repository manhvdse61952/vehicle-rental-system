package com.example.manhvdse61952.vrc_test_1.model;

import java.io.Serializable;

public class AccountObj implements Serializable {
    private String usernameOrEmail;
    private String password;

    public AccountObj(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
