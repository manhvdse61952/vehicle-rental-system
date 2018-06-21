package com.example.manhvdse61952.vrc_android.model.apiModel;

import java.io.Serializable;

public class Login implements Serializable {
    private String usernameOrEmail;
    private String password;

    public Login(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
