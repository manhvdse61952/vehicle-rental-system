package com.example.manhvdse61952.vrc_android.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.Signup;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupPolicyActivity extends AppCompatActivity {

    private String receiveValue = "";
    String imagePath = "";
    Signup signup = new Signup();
    Button btnAccept;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_policy);

        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get value from SignupRole activity
                Intent receiveIt = getIntent();
                receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
                imagePath = receiveIt.getStringExtra("PICTURE_FILE_PATH");
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    signup = objectMapper.readValue(receiveValue, Signup.class);
                    RetrofitCallAPI rfCall = new RetrofitCallAPI();
                    rfCall.SignupAccount(imagePath, receiveValue, SignupPolicyActivity.this);

                } catch (IOException e) {
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
