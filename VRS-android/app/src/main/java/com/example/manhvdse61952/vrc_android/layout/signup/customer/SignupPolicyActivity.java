package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.model.Signup;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SignupPolicyActivity extends AppCompatActivity {

    private String receiveValue = "";
    String imagePath = "";
    Signup signup = new Signup();
    Button btnAccept;
    CheckBox cbxSignupPolicy;

    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_policy);

        //Declare id
        btnAccept = (Button)findViewById(R.id.btnAccept);
        cbxSignupPolicy = (CheckBox)findViewById(R.id.cbxSignupPolicy);

        //Checkbox
        cbxSignupPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    btnAccept.setEnabled(true);
                    btnAccept.setBackground(getResources().getDrawable(R.drawable.btn_accept));
                } else {
                    btnAccept.setEnabled(false);
                    btnAccept.setBackground(getResources().getDrawable(R.drawable.btn_accept_hide));
                }
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get value from SignupRole activity
//                Intent receiveIt = getIntent();
//                receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
//                imagePath = receiveIt.getStringExtra("PICTURE_FILE_PATH");
//                ObjectMapper objectMapper = new ObjectMapper();
//                try {
//                    signup = objectMapper.readValue(receiveValue, Signup.class);
//                    dialog = ProgressDialog.show(SignupPolicyActivity.this, "Đăng ký",
//                            "Đang xử lý ...", true);
//                    RetrofitCallAPI rfCall = new RetrofitCallAPI();
//                    rfCall.SignupAccount(imagePath, receiveValue, SignupPolicyActivity.this, dialog);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                //Get value from shared preferences
                SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                String username = editor.getString("username", null);
                String password = editor.getString("password", null);
                String email = editor.getString("email", null);
                String name = editor.getString("name", null);
                String phone = editor.getString("phone", null);
                String cmnd = editor.getString("cmnd", null);
                String paypal = editor.getString("paypal", null);
                String address = editor.getString("address", null);
                String CMND_image_path = editor.getString("CMND_image_path", null);
                String rolename = editor.getString("rolename", null);

                //Call API
                ObjectMapper objectMapper = new ObjectMapper();
                Signup signupObj = new Signup(address, email, cmnd, CMND_image_path, name, password, paypal, phone, rolename, username);
                try {
                    String json = objectMapper.writeValueAsString(signupObj);
                    dialog = ProgressDialog.show(SignupPolicyActivity.this, "Đăng ký",
                            "Đang xử lý ...", true);
                    RetrofitCallAPI refCall = new RetrofitCallAPI();
                    refCall.SignupAccount(CMND_image_path, json, SignupPolicyActivity.this, dialog);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupPolicyActivity.this, SignupRoleActivity.class);
        startActivity(it);
    }
}
