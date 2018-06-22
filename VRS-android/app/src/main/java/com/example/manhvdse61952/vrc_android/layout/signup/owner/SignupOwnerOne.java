package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupRoleActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupOwnerOne extends AppCompatActivity {

    Button btnSignupVehicle1, btnSignupVehicle2, btnSignupVehicle3, btnSignupAccountBack;
    ProgressDialog dialog;
    int i = 1 + ImmutableValue.listVehicleMaker.size()/3 * 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_one);

        btnSignupVehicle1 = (Button) findViewById(R.id.btnSignupOwner1);
        btnSignupVehicle2 = (Button) findViewById(R.id.btnSignupOwner2);
        btnSignupVehicle3 = (Button) findViewById(R.id.btnSignupOwner3);
        btnSignupAccountBack = (Button) findViewById(R.id.btnSignupAccountBack);

        btnSignupVehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImmutableValue.listVehicleModelThree.size() == 0){
                    dialog = ProgressDialog.show(SignupOwnerOne.this, "Hệ thống",
                            "Vui lòng đợi ...", true);
                    ImmutableValue.listVehicleModelThree = new ArrayList<>();
                    getVehicleModelPartThree("XE_MAY");
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("vehicleType", "XE_MAY");
                    editor.apply();
                    Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                    startActivity(it);
                }
            }
        });

        btnSignupVehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImmutableValue.listVehicleModelThree.size() == 0){
                    dialog = ProgressDialog.show(SignupOwnerOne.this, "Hệ thống",
                            "Vui lòng đợi ...", true);
                    ImmutableValue.listVehicleModelThree = new ArrayList<>();
                    getVehicleModelPartThree("XE_CA_NHAN");
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("vehicleType", "XE_CA_NHAN");
                    editor.apply();
                    Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                    startActivity(it);
                }

            }
        });

        btnSignupVehicle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImmutableValue.listVehicleModelThree.size() == 0){
                    dialog = ProgressDialog.show(SignupOwnerOne.this, "Hệ thống",
                            "Vui lòng đợi ...", true);
                    ImmutableValue.listVehicleModelThree = new ArrayList<>();
                    getVehicleModelPartThree("XE_DU_LICH");
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("vehicleType", "XE_DU_LICH");
                    editor.apply();
                    Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                    startActivity(it);
                }
            }
        });

        btnSignupAccountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerOne.this, SignupRoleActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerOne.this, SignupRoleActivity.class);
        startActivity(it);
    }

    /////////////////////////////////////////////////////////
    private void getVehicleModelPartThree(final String roleValue) {
        if (i >= ImmutableValue.listVehicleMaker.size() - 1) {
            dialog.dismiss();
            SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
            editor.putString("vehicleType", roleValue);
            editor.apply();
            Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
            startActivity(it);
            return;
        }

        i++;
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleModel(ImmutableValue.listVehicleMaker.get(i).toString());

        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    for (int j = 0; j < response.body().size(); j++) {
                        ImmutableValue.listVehicleModelThree.add(ImmutableValue.listVehicleMaker.get(i).toString() +
                                " " + response.body().get(j).toString());
                    }
                }
                getVehicleModelPartThree(roleValue);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(SignupOwnerOne.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
