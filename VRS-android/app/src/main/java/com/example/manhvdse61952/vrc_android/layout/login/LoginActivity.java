package com.example.manhvdse61952.vrc_android.layout.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.layout.contract.ContractDetail;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupAccountActivity;
import com.example.manhvdse61952.vrc_android.layout.vehicle.VehicleDetail;
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

    int i = 0;


    TextInputLayout username_txt, password_txt;
    EditText edtUsername, edtPassword;

    ImmutableValue locationObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.splashScreenTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /// get address and vehicle maker data
        if (RetrofitCallAPI.lisCityTest.size() == 0) {
        dialog = ProgressDialog.show(LoginActivity.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        RetrofitCallAPI testAPI = new RetrofitCallAPI();
        testAPI.getAllAddress(dialog, LoginActivity.this);
        }


        //Declare id
        username_txt = (TextInputLayout) findViewById(R.id.username_txt);
        password_txt = (TextInputLayout) findViewById(R.id.password_txt);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Check location of user
        locationObj = new ImmutableValue();
        locationObj.checkAddressPermission(LoginActivity.this, LoginActivity.this);



        //Accept user go to main layout without login
        SharedPreferences editor = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        SharedPreferences editor2 = getSharedPreferences(ImmutableValue.IN_APP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int usernameID = editor.getInt("userID", 0);
        String vehicleID = editor2.getString("ID", "Empty");
        String contractStatus = editor2.getString("contractID", "Empty");
        if (usernameID != 0 && !contractStatus.equals("Empty")){
            Intent it = new Intent(LoginActivity.this, ContractDetail.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        } else if(usernameID != 0 && !vehicleID.equals("Empty")){
            Intent it = new Intent(LoginActivity.this, VehicleDetail.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }
        else if (usernameID != 0 && vehicleID.equals("Empty")){
            Intent it = new Intent(LoginActivity.this, activity_main_2.class);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);
        }

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
                if (RetrofitCallAPI.lisCityTest.size() != 0 && ImmutableValue.listVehicleMaker.size() != 0) {
                    Intent it = new Intent(LoginActivity.this, SignupAccountActivity.class);
                    startActivity(it);
                } else {
                    dialog = ProgressDialog.show(LoginActivity.this, "Đang xử lý",
                            "Vui lòng đợi ...", true);
                    RetrofitCallAPI testAPI = new RetrofitCallAPI();
                    testAPI.getAllAddress(dialog, LoginActivity.this);
                }

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
                } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
