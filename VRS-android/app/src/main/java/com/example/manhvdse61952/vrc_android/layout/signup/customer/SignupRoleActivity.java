package com.example.manhvdse61952.vrc_android.layout.signup.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.owner.SignupOwnerOne;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitCallAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupRoleActivity extends AppCompatActivity {

    ImageView imgCustomer, imgOwner;
    Button btnSignupAccountBack;
    int i = 0;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_role);

        imgCustomer = (ImageView) findViewById(R.id.customer_icon);
        btnSignupAccountBack = (Button) findViewById(R.id.btnSignupAccountBack);

        btnSignupAccountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupRoleActivity.this, SignupUserInfoActivity.class);
                startActivity(it);
            }
        });


        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                editor.putString("rolename", "ROLE_USER");
                editor.apply();
                Intent it = new Intent(SignupRoleActivity.this, SignupPolicyActivity.class);
                startActivity(it);

            }
        });

        imgOwner = (ImageView) findViewById(R.id.owner_icon);
        imgOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImmutableValue.listVehicleModelTwo.size() == 0){
                    dialog = ProgressDialog.show(SignupRoleActivity.this, "Hệ thống",
                            "Vui lòng đợi ...", true);

                    ImmutableValue.listVehicleModelTwo = new ArrayList<>();
                    getVehicleModelPartTwo();
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                    editor.putString("rolename", "ROLE_OWNER");
                    editor.apply();
                    Intent it = new Intent(SignupRoleActivity.this, SignupOwnerOne.class);
                    startActivity(it);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupRoleActivity.this, SignupUserInfoActivity.class);
        startActivity(it);
    }


    /////////////////////////////////////////////////////////
    private void getVehicleModelPartTwo() {
        if (i >= ImmutableValue.listVehicleMaker.size() / 2) {
            dialog.dismiss();
            SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
            editor.putString("rolename", "ROLE_OWNER");
            editor.apply();
            Intent it = new Intent(SignupRoleActivity.this, SignupOwnerOne.class);
            startActivity(it);
            return;
        }

        i++;
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleModel(ImmutableValue.listVehicleMaker.get(i).toString());

        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    for (int j = 0; j < response.body().size(); j++) {
                        ImmutableValue.listVehicleModelTwo.add(ImmutableValue.listVehicleMaker.get(i).toString() +
                                " " + response.body().get(j).toString());
                    }
                }
                getVehicleModelPartTwo();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(SignupRoleActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
