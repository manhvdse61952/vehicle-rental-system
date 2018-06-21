package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.Validate;

public class SignupAccountActivity extends AppCompatActivity {

    Button btnBack, btnNext;
    EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;
    TextInputLayout signup_username_txt, signup_password_txt, signup_email_txt;
    private String username = "", password = "", email = "";
    Validate validObj;

    RetrofitCallAPI rfCall = new RetrofitCallAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);

        //Declare id
        edtSignupUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);
        signup_username_txt = (TextInputLayout) findViewById(R.id.signup_username_txt);
        signup_password_txt = (TextInputLayout) findViewById(R.id.signup_password_txt);
        signup_email_txt = (TextInputLayout) findViewById(R.id.signup_email_txt);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);

        edtSignupUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                }
                else {
                    Toast.makeText(SignupAccountActivity.this, "OK", Toast.LENGTH_SHORT).show();


                }
            }
        });

//        edtSignupEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus){
//                }
//                else {
//                    Toast.makeText(SignupAccountActivity.this, "OK", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        //button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get value from edittext
                username = edtSignupUsername.getText().toString().trim();
                password = edtSignupPassword.getText().toString().trim();
                email = edtSignupEmail.getText().toString().trim();

                validObj = new Validate();
                Boolean checkUsername = validObj.validUsername(username, edtSignupUsername);
                Boolean checkPassword = validObj.validPassword(password, edtSignupPassword);
                Boolean checkEmail = validObj.validEmail(email, edtSignupEmail);
                if (checkUsername && checkPassword && checkEmail) {
                    //Call API
                    rfCall.checkExistedUsername(username, SignupAccountActivity.this);
                    rfCall.checkExistedEmail(email, SignupAccountActivity.this);
                    if (RetrofitCallAPI.emailResult == true && RetrofitCallAPI.usernameResult == true){
                        //shared preferences
                        SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                        editor.putString("username", username);
                        editor.putString("password", password);
                        editor.putString("email", email);
                        editor.apply();

                        Intent it = new Intent(SignupAccountActivity.this, SignupUserInfoActivity.class);
                        startActivity(it);
                    }
                }
            }
        });

        //Back button
        btnBack = (Button) findViewById(R.id.btnSignupAccountBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupAccountActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupAccountActivity.this, LoginActivity.class);
        startActivity(it);
    }
}
