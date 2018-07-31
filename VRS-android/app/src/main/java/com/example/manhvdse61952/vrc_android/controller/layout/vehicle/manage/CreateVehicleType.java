package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;

public class CreateVehicleType extends AppCompatActivity {

    Button btnSignupVehicle1, btnSignupVehicle2, btnSignupVehicle3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        btnSignupVehicle1 = (Button) findViewById(R.id.btnSignupOwner1);
        btnSignupVehicle2 = (Button) findViewById(R.id.btnSignupOwner2);
        btnSignupVehicle3 = (Button) findViewById(R.id.btnSignupOwner3);

        btnSignupVehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.VEHICLE_vehicleType, ImmutableValue.XE_MAY);
                editor.apply();
                Intent it = new Intent(CreateVehicleType.this, CreateVehicle.class);
                startActivity(it);
            }
        });

        btnSignupVehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.VEHICLE_vehicleType, ImmutableValue.XE_CA_NHAN);
                editor.apply();
                Intent it = new Intent(CreateVehicleType.this, CreateVehicle.class);
                startActivity(it);
            }
        });

        btnSignupVehicle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.VEHICLE_vehicleType, ImmutableValue.XE_DU_LICH);
                editor.apply();
                Intent it = new Intent(CreateVehicleType.this, CreateVehicle.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onBackPressed() {
        CreateVehicleType.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
