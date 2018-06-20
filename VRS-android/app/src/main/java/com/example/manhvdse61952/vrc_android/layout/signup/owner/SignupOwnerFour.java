package com.example.manhvdse61952.vrc_android.layout.signup.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupPolicyActivity;
import com.example.manhvdse61952.vrc_android.model.Account;
import com.example.manhvdse61952.vrc_android.model.Signup;
import com.example.manhvdse61952.vrc_android.model.Vehicle;
import com.example.manhvdse61952.vrc_android.model.VehicleInformation;
import com.example.manhvdse61952.vrc_android.payload.VehiclePayload;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupOwnerFour extends AppCompatActivity {

    Button btnVehicleNext, btnVehicleBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_four);


        btnVehicleNext = (Button) findViewById(R.id.btnVehicleNext);
        btnVehicleBack = (Button) findViewById(R.id.btnVehicleBack);
        //Next button
        btnVehicleNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerFour.this, SignupOwnerPolicy.class);
                startActivity(it);

            }
        });

        //Back button
        btnVehicleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerFour.this, SignupOwnerThree.class);
                startActivity(it);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerFour.this, SignupOwnerThree.class);
        startActivity(it);
    }

}
