package com.example.manhvdse61952.vrc_android.controller.layout.signup.owner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.model.api_model.Signup;
import com.example.manhvdse61952.vrc_android.model.api_model.Vehicle;
import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupOwnerPolicy extends AppCompatActivity {

    Button btnSignupAccept;
    CheckBox cbxSignupPolicy;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_policy_owner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        btnSignupAccept = (Button) findViewById(R.id.btnSignupAccept);
        cbxSignupPolicy = (CheckBox) findViewById(R.id.cbxSignupPolicy);

        //Checkbox
        cbxSignupPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    btnSignupAccept.setEnabled(true);
                    btnSignupAccept.setBackground(getResources().getDrawable(R.drawable.border_green_primarygreen));
                } else {
                    btnSignupAccept.setEnabled(false);
                    btnSignupAccept.setBackground(getResources().getDrawable(R.drawable.border_green_hide));
                }
            }
        });

        //Accept button
        btnSignupAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptAction();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerPolicy.this, RegistVehicle.class);
        startActivity(it);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void acceptAction(){
        dialog = ProgressDialog.show(SignupOwnerPolicy.this, "Đăng ký",
                "Đang xử lý ...", true);
        //Signup for customer
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String username = editor.getString(ImmutableValue.SIGNUP_username, null);
        String password = editor.getString(ImmutableValue.SIGNUP_password, null);
        String email = editor.getString(ImmutableValue.SIGNUP_email, null);
        String name = editor.getString(ImmutableValue.SIGNUP_fullName, null);
        String phone = editor.getString(ImmutableValue.SIGNUP_phone, null);
        String cmnd = editor.getString(ImmutableValue.SIGNUP_cmnd, null);
        String paypal = "";
        //String address = editor.getString("address", null);
        String CMND_image_path = editor.getString(ImmutableValue.SIGNUP_img_CMND, null);
        String rolename = editor.getString(ImmutableValue.SIGNUP_role, null);

        //call api
        ObjectMapper objectMapper = new ObjectMapper();
        Signup signupObj = new Signup("", email, cmnd, CMND_image_path, name, password, paypal, phone, rolename, username);
        try {
            String json = objectMapper.writeValueAsString(signupObj);
            Retrofit retrofit = RetrofitConfig.getClient();
            final AccountAPI accountAPI = retrofit.create(AccountAPI.class);
            String IMG_JPEG = "image/jpeg";
            File imageFile = new File(CMND_image_path);
            RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);
            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), json);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), fileBody);
            Call<ResponseBody> responseBodyCall = accountAPI.signup(data, body);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        JSONObject testObj = null;
                        try {
                            testObj = new JSONObject(response.body().string());
                            String userID = testObj.get("message").toString();
                            createVehicle(userID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(SignupOwnerPolicy.this, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(SignupOwnerPolicy.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void createVehicle(String userID) {
        //Get vehicle value from shared preferences
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString(ImmutableValue.VEHICLE_frameNumber, null);
        int vehicleInformationID = editor.getInt(ImmutableValue.VEHICLE_informationID, 0);
        int districtID = editor.getInt(ImmutableValue.VEHICLE_districtID, 0);
        String description = "";
        String rentFeePerSlot = "0";
        String rentFeePerDay = editor.getString(ImmutableValue.VEHICLE_rentFeePerDay, null);
        String rentFeePerHours = editor.getString(ImmutableValue.VEHICLE_rentFeePerHours, null);
        String depositFee = editor.getString(ImmutableValue.VEHICLE_depositFee, null);
        String plateNumber = editor.getString(ImmutableValue.VEHICLE_plateNumber, null);
        int requireHouseHold = editor.getInt(ImmutableValue.VEHICLE_requireHouseHold, 0);
        int requireIdCard = editor.getInt(ImmutableValue.VEHICLE_requireIdCard, 0);
        int isGasoline = editor.getInt(ImmutableValue.VEHICLE_isGasoline, 1);
        int isManual = editor.getInt(ImmutableValue.VEHICLE_isManual, 1);

        String imagePath = editor.getString(ImmutableValue.VEHICLE_img_frameNumber, null);
        String imageVehicle1 = editor.getString(ImmutableValue.VEHICLE_img_vehicle_1, null);
        String imageVehicle2 = editor.getString(ImmutableValue.VEHICLE_img_vehicle_2, null);


        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicleObj = new Vehicle(frameNumber, Integer.valueOf(userID), vehicleInformationID, description, Float.valueOf(rentFeePerSlot),
                Float.valueOf(rentFeePerDay), Float.valueOf(rentFeePerHours), Float.valueOf(depositFee), plateNumber,
                requireHouseHold, requireIdCard, districtID, isGasoline, isManual);

        try {
            String json = objectMapper.writeValueAsString(vehicleObj);
            String IMG_JPEG = "image/jpeg";
            File imageFile = new File(imagePath);
            File imageVehicleFile1 = new File(imageVehicle1);
            File imageVehicleFile2 = new File(imageVehicle2);

            RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);
            RequestBody fileBody2 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile1);
            RequestBody fileBody3 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile2);

            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), json);

            MultipartBody.Part body = MultipartBody.Part.createFormData("files", imageFile.getName(), fileBody);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("files", imageVehicleFile1.getName(), fileBody2);
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("files", imageVehicleFile2.getName(), fileBody3);
            MultipartBody.Part[] imagesParts = new MultipartBody.Part[3];
            imagesParts[0] = body;
            imagesParts[1] = body2;
            imagesParts[2] = body3;

            Retrofit test = RetrofitConfig.getClient();
            final VehicleAPI testAPI = test.create(VehicleAPI.class);
            Call<ResponseBody> responseBodyCall = testAPI.createVehicle(data, imagesParts);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200){
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignupOwnerPolicy.this);
                        builder.setMessage("Chúng tôi sẽ gửi email cho bạn sau khi xác thực tài khoản thành công, thời gian khoảng từ 15-20 phút ! Cảm ơn bạn đã đăng ký VRS");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences settings = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                settings.edit().clear().commit();
                                Intent it = new Intent(SignupOwnerPolicy.this, LoginActivity.class);
                                startActivity(it);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(SignupOwnerPolicy.this, "Đã xả ra lỗi! Hãy đăng ký tài khoản khác", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(SignupOwnerPolicy.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
