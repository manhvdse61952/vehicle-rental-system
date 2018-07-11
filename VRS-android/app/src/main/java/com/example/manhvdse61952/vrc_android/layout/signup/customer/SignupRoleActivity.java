package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerOne;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRoleActivity extends AppCompatActivity {

    ImageView imgCustomer, imgOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_role);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        imgCustomer = (ImageView) findViewById(R.id.customer_icon);

        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("rolename", "ROLE_USER");
                editor.apply();
                Intent it = new Intent(SignupRoleActivity.this, SignupPolicyActivity.class);
                startActivity(it);

            }
        });

        imgOwner = (ImageView) findViewById(R.id.owner_icon);
        imgOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("rolename", "ROLE_OWNER");
                editor.apply();

                SharedPreferences.Editor editor2 = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor2.putString("vehicleMaker", "Empty");
                editor2.putString("vehicleModel", "Empty");
                editor2.putString("img_vehicle_1", "");
                editor2.putString("img_vehicle_2", "");
                editor2.putString("picture_path", "");
                editor2.putInt("yearPosition", -1);
                editor2.putString("frameNumber", "");
                editor2.putString("plateNumber", "");
                editor2.putInt("cityPosition", -1);
                editor2.putInt("districtPosition", -1);
                editor2.putString("rentFeePerHours", "");
                editor2.putString("rentFeePerDay", "");
                editor2.putString("depositFee", "");
                editor2.putInt("isGasoline", -1);
                editor2.putInt("isManual", -1);
                editor2.putInt("requireHouseHold", 0);
                editor2.putInt("requireIdCard", 0);
                editor2.apply();
                Intent it = new Intent(SignupRoleActivity.this, SignupOwnerOne.class);
                startActivity(it);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupRoleActivity.this, SignupUserInfoActivity.class);
        startActivity(it);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
