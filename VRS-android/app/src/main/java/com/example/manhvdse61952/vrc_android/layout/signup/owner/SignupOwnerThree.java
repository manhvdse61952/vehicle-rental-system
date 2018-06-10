package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_android.R;

public class SignupOwnerThree extends AppCompatActivity {

    Button btnNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_three);

        btnNext = (Button)findViewById(R.id.btnVehicleNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerThree.this, SignupOwnerFour.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerThree.this, SignupOwnerTwo.class);
        startActivity(it);
    }
}
