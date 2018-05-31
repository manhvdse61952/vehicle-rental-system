package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_test_1.R;
import com.example.manhvdse61952.vrc_test_1.model.SignupObj;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SignupUserInfoActivity extends AppCompatActivity{

    Button btnBack, btnNext, btnPictureCMND;
    String receiveValue = "";
    private String MESSAGE_CODE_PREVIOUS = "SignupAccountActivityToSignupInfoActivity";
    private String MESSAGE_CODE_NEXT = "SignupInfoToSignupRole";
    SignupObj signupObj = new SignupObj();

    EditText edtSignupName, edtSignupPhone, edtSignupCNMD, edtSignupPaypal;
    Spinner spnAddress;
    String name = "", phone= "", cmnd = "", paypal = "", address = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_userinfo);


        Intent receiveIt = getIntent();
        receiveValue = receiveIt.getStringExtra(MESSAGE_CODE_PREVIOUS);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
             signupObj = objectMapper.readValue(receiveValue, SignupObj.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        btnBack = (Button) findViewById(R.id.btnSignupAccountBack);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
        btnPictureCMND = (Button)findViewById(R.id.btnPictureCMND);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edtSignupName = (EditText)findViewById(R.id.edtSignupName);
                edtSignupPhone = (EditText)findViewById(R.id.edtSignupPhone);
                edtSignupCNMD = (EditText)findViewById(R.id.edtSignupCNMD);
                edtSignupPaypal = (EditText)findViewById(R.id.edtSignupPaypal);
                spnAddress = (Spinner)findViewById(R.id.spnAddress);

                name = edtSignupName.getText().toString();
                phone = edtSignupPhone.getText().toString();
                cmnd = edtSignupCNMD.getText().toString();
                paypal = edtSignupPaypal.getText().toString();
                address = spnAddress.getSelectedItem().toString();

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    Intent it = new Intent(SignupUserInfoActivity.this, SignupRoleActivity.class);
                    signupObj.setName(name);
                    signupObj.setPhone(phone);
                    signupObj.setIdCard(cmnd);
                    signupObj.setPaypalID(paypal);
                    signupObj.setAddress(address);
                    String json = objectMapper.writeValueAsString(signupObj);
                    it.putExtra(MESSAGE_CODE_NEXT, json);
                    Toast.makeText(SignupUserInfoActivity.this, name, Toast.LENGTH_SHORT).show();
                    startActivity(it);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it2 = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
                startActivity(it2);
            }
        });
        btnPictureCMND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it3 = new Intent(SignupUserInfoActivity.this, SignupUserInfoCameraActivity.class);
                startActivity(it3);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupUserInfoActivity.this, SignupAccountActivity.class);
        startActivity(it);
    }

}
