package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_test_1.R;
import com.example.manhvdse61952.vrc_test_1.interfaceAPI.AccountITF;
import com.example.manhvdse61952.vrc_test_1.interfaceAPI.SignupITF;
import com.example.manhvdse61952.vrc_test_1.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_test_1.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_test_1.model.AccountObj;
import com.example.manhvdse61952.vrc_test_1.model.SignupObj;
import com.example.manhvdse61952.vrc_test_1.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_test_1.remote.RetrofitConnect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRoleActivity extends AppCompatActivity {

    //private String MESSAGE_CODE_PREVIOUS = "SignupInfoToSignupRole";
    //private String MESSAGE_CODE_NEXT = "SignupRoleToSignupPolicy";
    String receiveValue = "";
    SignupObj signupObj = new SignupObj();
    ImageView imgCustomer, imgOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_role);

        imgCustomer = (ImageView)findViewById(R.id.customer_icon);
        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent receiveIt = getIntent();
                receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    signupObj = objectMapper.readValue(receiveValue, SignupObj.class);
                    signupObj.setRolename("ROLE_USER");

                    Intent it = new Intent(SignupRoleActivity.this, SignupPolicyActivity.class);
                    it.putExtra(ImmutableValue.MESSAGE_CODE, objectMapper.writeValueAsString(signupObj));
                    startActivity(it);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        imgOwner = (ImageView)findViewById(R.id.owner_icon);
        imgOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
