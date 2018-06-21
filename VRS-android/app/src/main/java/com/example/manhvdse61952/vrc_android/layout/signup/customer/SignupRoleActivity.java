package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerOne;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;

public class SignupRoleActivity extends AppCompatActivity {

    ImageView imgCustomer, imgOwner;
    Button btnSignupAccountBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_role);

        imgCustomer = (ImageView) findViewById(R.id.customer_icon);
        btnSignupAccountBack = (Button)findViewById(R.id.btnSignupAccountBack);

        btnSignupAccountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupRoleActivity.this, SignupUserInfoActivity.class);
                startActivity(it);
            }
        });

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
}
