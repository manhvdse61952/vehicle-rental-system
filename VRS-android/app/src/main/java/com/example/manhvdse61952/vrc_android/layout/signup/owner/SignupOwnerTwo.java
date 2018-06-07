package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_android.R;

public class SignupOwnerTwo extends AppCompatActivity {

    Button btnNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_two);

        btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerThree.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerTwo.this, SignupOwnerOne.class);
        startActivity(it);
    }
}
