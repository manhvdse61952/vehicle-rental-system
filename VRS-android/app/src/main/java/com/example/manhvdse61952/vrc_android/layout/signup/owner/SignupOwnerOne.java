package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupRoleActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class SignupOwnerOne extends AppCompatActivity {

    Button btnSignupVehicle1, btnSignupVehicle2, btnSignupVehicle3, btnSignupAccountBack;
    private final String VEHICLE_TYPE = "VEHICLE_TYPE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_one);

        btnSignupVehicle1 = (Button) findViewById(R.id.btnSignupOwner1);
        btnSignupVehicle2 = (Button) findViewById(R.id.btnSignupOwner2);
        btnSignupVehicle3 = (Button) findViewById(R.id.btnSignupOwner3);
        btnSignupAccountBack = (Button)findViewById(R.id.btnSignupAccountBack);

        btnSignupVehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
//                editor.putString("vehicleType", "XE_MAY");
//                editor.apply();
//                Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
//                startActivity(it);
            }
        });

        btnSignupVehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("vehicleType", "XE_CA_NHAN");
                editor.apply();
                Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                startActivity(it);
            }
        });

        btnSignupVehicle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
//                editor.putString("vehicleType", "XE_DU_LICH");
//                editor.apply();
//                Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
//                startActivity(it);
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
}
