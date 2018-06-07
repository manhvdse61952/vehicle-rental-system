package com.example.manhvdse61952.vrc_android.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerOne;
import com.example.manhvdse61952.vrc_android.model.Signup;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SignupRoleActivity extends AppCompatActivity {

    //private String MESSAGE_CODE_PREVIOUS = "SignupInfoToSignupRole";
    //private String MESSAGE_CODE_NEXT = "SignupRoleToSignupPolicy";
    String receiveValue = "", imagePath = "";
    Signup signup = new Signup();
    ImageView imgCustomer, imgOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_role);

        imgCustomer = (ImageView) findViewById(R.id.customer_icon);
        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent receiveIt = getIntent();
                receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
                imagePath = receiveIt.getStringExtra("PICTURE_FILE_PATH");
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    signup = objectMapper.readValue(receiveValue, Signup.class);
                    signup.setRolename("ROLE_USER");

                    Intent it = new Intent(SignupRoleActivity.this, SignupPolicyActivity.class);
                    it.putExtra(ImmutableValue.MESSAGE_CODE, objectMapper.writeValueAsString(signup));
                    it.putExtra("PICTURE_FILE_PATH", imagePath);
                    startActivity(it);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        imgOwner = (ImageView) findViewById(R.id.owner_icon);
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
