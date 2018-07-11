package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    Button btnSignupAccountClean, btnNext;
    EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;
    TextInputLayout signup_username_txt, signup_password_txt, signup_email_txt;
    private String username = "", password = "", email = "";
    Validate validObj;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        edtSignupUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);
        signup_username_txt = (TextInputLayout) findViewById(R.id.signup_username_txt);
        signup_password_txt = (TextInputLayout) findViewById(R.id.signup_password_txt);
        signup_email_txt = (TextInputLayout) findViewById(R.id.signup_email_txt);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);

        revertValue();

        //button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextAction();
            }
        });

        //Back clean
        btnSignupAccountClean = (Button) findViewById(R.id.btnSignupAccountClean);
        btnSignupAccountClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSignupUsername.setText("");
                edtSignupEmail.setText("");
                edtSignupPassword.setText("");
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupAccountActivity.this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void revertValue(){
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String getUsername = editor.getString("username", "");
        String getPassword = editor.getString("password", "");
        String getEmail = editor.getString("email", "");
        if (!getUsername.trim().equals("")){
            edtSignupUsername.setText(getUsername);
        }
        if (!getPassword.trim().equals("")){
            edtSignupPassword.setText(getPassword);
        }
        if (!getEmail.trim().equals("")){
            edtSignupEmail.setText(getEmail);
        }
    }

    private void nextAction(){
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
            dialog = ProgressDialog.show(SignupAccountActivity.this, "Đăng ký",
                    "Đang kiểm tra ...", true);
            RetrofitCallAPI rfCall = new RetrofitCallAPI();
            rfCall.checkExistedUsername(username, password, email, SignupAccountActivity.this, edtSignupUsername, edtSignupEmail, dialog);

        }
    }
}
