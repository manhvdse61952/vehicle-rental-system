package com.example.manhvdse61952.vrc_android.controller.layout.vehicle.manage;

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
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Vehicle;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PolicyVehicle extends AppCompatActivity {
    Button btn_create_vehiclel;
    CheckBox cbxSignupPolicy;
    private DatabaseReference dbr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_vehicle);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        declareID();
        initLayout();
    }

    private void declareID(){
        btn_create_vehiclel = (Button)findViewById(R.id.btn_create_vehicle);
        cbxSignupPolicy = (CheckBox)findViewById(R.id.cbxSignupPolicy);
    }

    private void initLayout(){
        //Checkbox
        cbxSignupPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    btn_create_vehiclel.setEnabled(true);
                    btn_create_vehiclel.setBackground(getResources().getDrawable(R.drawable.border_green_primarygreen));
                } else {
                    btn_create_vehiclel.setEnabled(false);
                    btn_create_vehiclel.setBackground(getResources().getDrawable(R.drawable.border_green_hide));
                }
            }
        });

        btn_create_vehiclel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences editor = getSharedPreferences("ValueTemp", MODE_PRIVATE);
                String frameNumber = editor.getString("frameNumber", "");
                int vehicleInformationID = editor.getInt("vehicleInformationID", 0);
                String rentFeePerDay = editor.getString("rentFeePerDay", "");
                String rentFeePerHours = editor.getString("rentFeePerHours", "");
                String depositFee = editor.getString("depositFee", "");
                String plateNumber = editor.getString("plateNumber", "");
                int requireHouseHold = editor.getInt("requireHouseHold", 0);
                int requireIdCard = editor.getInt("requireIdCard", 0);
                int districtID = editor.getInt("districtID", 0);
                int isGasoline = editor.getInt("isGasoline", 0);
                int isManual = editor.getInt("isManual", 0);
                String picture_path = editor.getString("picture_path", "");
                String img_vehicle_1 = editor.getString("img_vehicle_1", "");
                String img_vehicle_2 = editor.getString("img_vehicle_2", "");
                Boolean haveDriver = editor.getBoolean("haveDriver", true);
                int receiveType = editor.getInt("receiveType", 0);
                Boolean monday = editor.getBoolean("monday", false);
                Boolean tuesday = editor.getBoolean("tuesday", false);
                Boolean wednesday = editor.getBoolean("wednesday", false);
                Boolean thursday = editor.getBoolean("thursday", false);
                Boolean friday = editor.getBoolean("friday", false);
                Boolean saturday = editor.getBoolean("saturday", false);
                Boolean sunday = editor.getBoolean("sunday", false);
                double longitude = Double.parseDouble(editor.getString("longitude", "0"));
                double latitude = Double.parseDouble(editor.getString("latitude", "0"));
                createNewVehicle(frameNumber, vehicleInformationID, rentFeePerDay, rentFeePerHours,
                        depositFee, plateNumber, requireHouseHold, requireIdCard, districtID, isGasoline, isManual,
                        picture_path, img_vehicle_1, img_vehicle_2, haveDriver, receiveType, monday,
                        tuesday, wednesday, thursday, friday, saturday, sunday,
                        longitude, latitude);
            }
        });
    }


    private void createNewVehicle(final String frameNumber, int vehicleInformationID, String rentFeePerDay, String rentFeePerHours,
                                  String depositFee, String plateNumber, int requireHouseHold, int requireIdCard, int districtID,
                                  int isGasoline, int isManual, String img_vehicle_1, String img_vehicle_2,
                                  String picture_path, Boolean haveDriver, int receiveType, final Boolean monday, final Boolean tuesday, final Boolean wednesday,
                                  final Boolean thursday, final Boolean friday, final Boolean saturday, final Boolean sunday
    , final double longitude, final double latitude){
        final ProgressDialog dialog = ProgressDialog.show(PolicyVehicle.this, "Đang xử lý",
                "Vui lòng đợi ...", true);
        SharedPreferences editor = getSharedPreferences(ImmutableValue.HOME_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        int userID = editor.getInt(ImmutableValue.HOME_userID, 0);
        String description = "";
        String rentFeePerSlot = "0";

        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle vehicleObj = new Vehicle(frameNumber, userID, vehicleInformationID, description, Float.valueOf(rentFeePerSlot),
                Float.valueOf(rentFeePerDay), Float.valueOf(rentFeePerHours), Float.valueOf(depositFee), plateNumber,
                requireHouseHold, requireIdCard, districtID, isGasoline, isManual, longitude, latitude, haveDriver, receiveType
                , monday, tuesday, wednesday, thursday, friday, saturday, sunday);

        try {
            String json = objectMapper.writeValueAsString(vehicleObj);
            String IMG_JPEG = "image/jpeg";
            File imageVehicleFile1 = new File(img_vehicle_1);
            File imageVehicleFile2 = new File(img_vehicle_2);
            File imageFile = new File(picture_path);


            RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile1);
            RequestBody fileBody2 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile2);
            RequestBody fileBody3 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);

            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), json);

            MultipartBody.Part body = MultipartBody.Part.createFormData("files", imageVehicleFile1.getName(), fileBody);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("files", imageVehicleFile2.getName(), fileBody2);
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("files", imageFile.getName(), fileBody3);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(PolicyVehicle.this);
                        builder.setMessage("Yêu cầu đăng kí xe thành công! Bạn vui lòng đợi từ 3-5 phút để chúng tôi xác nhận và gửi thông báo về thiết bị");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences settings = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                settings.edit().clear().commit();
                                SharedPreferences settings2 = getSharedPreferences(ImmutableValue.MAIN_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                settings2.edit().clear().commit();
                                SharedPreferences settings3 = getSharedPreferences("ValueTemp", MODE_PRIVATE);
                                settings3.edit().clear().commit();
                                dbr = FirebaseDatabase.getInstance().getReference("Locations").child(frameNumber);
                                dbr.child("latitude").setValue(latitude);
                                dbr.child("longitude").setValue(longitude);

                                PolicyVehicle.this.finish();
                                Intent it = new Intent(PolicyVehicle.this, MainActivity.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(it);
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(PolicyVehicle.this, "Đã xả ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(PolicyVehicle.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


}
