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
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupAccountActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupRoleActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.RegistVehicle;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerOne;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerPolicy;
import com.example.manhvdse61952.vrc_android.model.apiModel.City;
import com.example.manhvdse61952.vrc_android.model.apiModel.Login;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.example.manhvdse61952.vrc_android.remote.Validate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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


        //Clear shared preferences
        SharedPreferences settings = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings.edit().clear().commit();

        //Declare id
        username_txt = (TextInputLayout) findViewById(R.id.username_txt);
        password_txt = (TextInputLayout) findViewById(R.id.password_txt);
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        //Check location of user
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

//    private void getVehicleModelPartOne() {
//        if (ImmutableValue.listVehicleMaker.size() != 0) {
//            if (i >= ImmutableValue.listVehicleMaker.size() / 3) {
//                dialog.dismiss();
//                Intent it = new Intent(LoginActivity.this, SignupAccountActivity.class);
//                startActivity(it);
//                return;
//            }
//
//            i++;
//            Retrofit test = RetrofitConnect.getClient();
//            final VehicleAPI testAPI = test.create(VehicleAPI.class);
//            Call<List<String>> responseBodyCall = testAPI.getVehicleModel(ImmutableValue.listVehicleMaker.get(i).toString());
//
//            responseBodyCall.enqueue(new Callback<List<String>>() {
//                @Override
//                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                    if (response.body() != null) {
//                        for (int j = 0; j < response.body().size(); j++) {
//                            ImmutableValue.listVehicleModelOne.add(ImmutableValue.listVehicleMaker.get(i).toString() +
//                                    " " + response.body().get(j).toString());
//                        }
//                    }
//                    getVehicleModelPartOne();
//                }
//
//                @Override
//                public void onFailure(Call<List<String>> call, Throwable t) {
//                    dialog.dismiss();
//                    Toast.makeText(LoginActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            dialog = ProgressDialog.show(LoginActivity.this, "Hệ thống",
//                    "Vui lòng đợi ...", true);
//            ImmutableValue.listVehicleModelOne = new ArrayList<>();
//            getVehicleModelPartOne();
//        }
//
//    }
}
