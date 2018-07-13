package com.example.manhvdse61952.vrc_android.controller.layout.signup.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.controller.layout.signup.owner.SignupOwnerOne;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;

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
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.SIGNUP_role, ImmutableValue.ROLE_USER);
                editor.apply();
                Intent it = new Intent(SignupRoleActivity.this, SignupPolicyActivity.class);
                startActivity(it);

            }
        });

        imgOwner = (ImageView) findViewById(R.id.owner_icon);
        imgOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString(ImmutableValue.SIGNUP_role, ImmutableValue.ROLE_OWNER);
                editor.putString(ImmutableValue.VEHICLE_vehicleMaker, "Empty");
                editor.putString(ImmutableValue.VEHICLE_vehicleModel, "Empty");
                editor.putString(ImmutableValue.VEHICLE_img_vehicle_1, "");
                editor.putString(ImmutableValue.VEHICLE_img_vehicle_2, "");
                editor.putString(ImmutableValue.VEHICLE_img_frameNumber, "");
                editor.putInt(ImmutableValue.VEHICLE_year, -1);
                editor.putString(ImmutableValue.VEHICLE_frameNumber, "");
                editor.putString(ImmutableValue.VEHICLE_plateNumber, "");
                editor.putInt(ImmutableValue.VEHICLE_city, -1);
                editor.putInt(ImmutableValue.VEHICLE_district, -1);
                editor.putString(ImmutableValue.VEHICLE_rentFeePerHours, "");
                editor.putString(ImmutableValue.VEHICLE_rentFeePerDay, "");
                editor.putString(ImmutableValue.VEHICLE_depositFee, "");
                editor.putInt(ImmutableValue.VEHICLE_isGasoline, -1);
                editor.putInt(ImmutableValue.VEHICLE_isManual, -1);
                editor.putInt(ImmutableValue.VEHICLE_requireHouseHold, 0);
                editor.putInt(ImmutableValue.VEHICLE_requireIdCard, 0);
                editor.apply();
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
