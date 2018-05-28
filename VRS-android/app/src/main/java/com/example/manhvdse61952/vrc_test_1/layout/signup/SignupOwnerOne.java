package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_test_1.R;

public class SignupOwnerOne extends AppCompatActivity {

    Button btnSignupVehicle1, btnSignupVehicle2, btnSignupVehicle3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_one);

        btnSignupVehicle1 = (Button)findViewById(R.id.btnSignupOwner1);
        btnSignupVehicle2 = (Button)findViewById(R.id.btnSignupOwner2);
        btnSignupVehicle3 = (Button)findViewById(R.id.btnSignupOwner3);

        btnSignupVehicle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                startActivity(it);
            }
        });

        btnSignupVehicle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                startActivity(it);
            }
        });

        btnSignupVehicle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerOne.this, SignupOwnerTwo.class);
                startActivity(it);
            }
        });
    }
}
