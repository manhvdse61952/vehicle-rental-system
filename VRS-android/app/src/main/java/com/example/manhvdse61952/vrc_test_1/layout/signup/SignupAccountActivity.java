package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_test_1.interfaceAPI.AccountITF;
import com.example.manhvdse61952.vrc_test_1.interfaceAPI.CheckDuplicatedUsername;
import com.example.manhvdse61952.vrc_test_1.interfaceAPI.SignupITF;
import com.example.manhvdse61952.vrc_test_1.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_test_1.R;
import com.example.manhvdse61952.vrc_test_1.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_test_1.model.AccountObj;
import com.example.manhvdse61952.vrc_test_1.model.SignupObj;
import com.example.manhvdse61952.vrc_test_1.remote.RetrofitConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupAccountActivity extends AppCompatActivity {

    Button btnBack, btnNext;
    EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;
    private String username = "", password = "", email = "";
    private String MESSAGE_CODE = "SignupAccountActivityToSignupInfoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);

        //Edit text
        edtSignupUsername = (EditText)findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText)findViewById(R.id.edtSignupPassword);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);

        btnNext = (Button)findViewById(R.id.btnSignupAccountNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtSignupUsername.getText().toString();
                password = edtSignupPassword.getText().toString();
                email = edtSignupEmail.getText().toString();
                Intent it = new Intent(SignupAccountActivity.this, SignupUserInfoActivity.class);
                ObjectMapper objectMapper = new ObjectMapper();
                SignupObj signupObj = new SignupObj(username, password, email);
                try {
                    String json = objectMapper.writeValueAsString(signupObj);
                    it.putExtra(MESSAGE_CODE, json);
                    startActivity(it);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }


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
