package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.manhvdse61952.vrc_test_1.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_test_1.R;

public class SignupAccountActivity extends AppCompatActivity {

    Button btnBack, btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);

        btnNext = (Button)findViewById(R.id.btnSignupAccountNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupAccountActivity.this, SignupUserInfoActivity.class);
                startActivity(it);
            }
        });

        btnBack = (Button)findViewById(R.id.btnSignupAccountBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(SignupAccountActivity.this, LoginActivity.class);
                startActivity(it2);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it3 = new Intent(SignupAccountActivity.this, LoginActivity.class);
        startActivity(it3);
    }
}
