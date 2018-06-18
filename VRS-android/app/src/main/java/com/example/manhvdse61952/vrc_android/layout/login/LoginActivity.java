package com.example.manhvdse61952.vrc_android.layout.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupAccountActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.Validate;

public class LoginActivity extends AppCompatActivity {

    TextView txtSignUp;
    Button btnLogin;

    String username = "";
    String password = "";
    ProgressDialog dialog;
    Validate validObj;

    TextInputLayout username_txt, password_txt;
    EditText edtUsername, edtPassword;

    ImmutableValue locationObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Clear shared preferences
        SharedPreferences settings = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();

        //Declare id
        username_txt = (TextInputLayout) findViewById(R.id.username_txt);
        password_txt = (TextInputLayout) findViewById(R.id.password_txt);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        locationObj = new ImmutableValue();
        locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);
        //Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();

                validObj = new Validate();
                locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);
                Boolean checkUsername = validObj.validUsername(username, edtUsername);
                Boolean checkPassword = validObj.validPassword(password, edtPassword);
                if (checkUsername && checkPassword) {
                    dialog = ProgressDialog.show(LoginActivity.this, "Đăng nhập",
                            "Đang xác thực ...", true);
                    RetrofitCallAPI rfCall = new RetrofitCallAPI();
                    rfCall.checkLogin(username, password, LoginActivity.this, dialog);
                }

            }
        });

        //Sign up link
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, SignupAccountActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ImmutableValue.REQUEST_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationObj = new ImmutableValue();
                    locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);
                } else if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }
}
