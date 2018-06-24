package com.example.manhvdse61952.vrc_android.layout.signup.owner;

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
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.model.apiModel.Signup;
import com.example.manhvdse61952.vrc_android.model.apiModel.Vehicle_New;
import com.example.manhvdse61952.vrc_android.payload.VehiclePayload;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
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

    Button btnSignupAccept, btnSignupBack;
    CheckBox cbxSignupPolicy;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_policy_owner);

        //Declare id
        btnSignupAccept = (Button) findViewById(R.id.btnSignupAccept);
        btnSignupBack = (Button) findViewById(R.id.btnSignupBack);
        cbxSignupPolicy = (CheckBox) findViewById(R.id.cbxSignupPolicy);

        //Checkbox
        cbxSignupPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    btnSignupAccept.setEnabled(true);
                    btnSignupAccept.setBackground(getResources().getDrawable(R.drawable.btn_accept));
                } else {
                    btnSignupAccept.setEnabled(false);
                    btnSignupAccept.setBackground(getResources().getDrawable(R.drawable.btn_accept_hide));
                }
            }
        });

        //Accept button
        btnSignupAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = ProgressDialog.show(SignupOwnerPolicy.this, "Đăng ký",
                        "Đang xử lý ...", true);
                //Signup for customer
                SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                String username = editor.getString("username", null);
                String password = editor.getString("password", null);
                String email = editor.getString("email", null);
                String name = editor.getString("name", null);
                String phone = editor.getString("phone", null);
                String cmnd = editor.getString("cmnd", null);
                String paypal = editor.getString("paypal", null);
                //String address = editor.getString("address", null);
                String CMND_image_path = editor.getString("CMND_image_path", null);
                String rolename = editor.getString("rolename", null);

                //call api
                ObjectMapper objectMapper = new ObjectMapper();
                Signup signupObj = new Signup("", email, cmnd, CMND_image_path, name, password, paypal, phone, rolename, username);
                try {
                    String json = objectMapper.writeValueAsString(signupObj);
                    Retrofit retrofit = RetrofitConnect.getClient();
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
                            JSONObject testObj = null;
                            try {
                                testObj = new JSONObject(response.body().string());
                                SharedPreferences.Editor editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE).edit();
                                editor.putString("user-id", testObj.get("message").toString());
                                editor.apply();
                                SharedPreferences editor2 = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                                createVehicle(editor2.getString("user-id", "37"));
                            } catch (Exception e) {

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
        });

        //Back button
        btnSignupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SignupOwnerPolicy.this, SignupOwnerFour.class);
                startActivity(it);
            }
        });
    }


    // Missing user ID, and i'll use it on the btnAccept click listener
    public void createVehicle(String userID) {
        //Get vehicle value from shared preferences
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString("frameNumber", null);
        int vehicleInformationID = editor.getInt("vehicleInformationID", 0);
        int districtID = editor.getInt("districtID", 0);
        String description = editor.getString("description", "");
        String rentFeePerSlot = editor.getString("rentFeePerSlot", null);
        String rentFeePerDay = editor.getString("rentFeePerDay", null);
        String rentFeePerHours = editor.getString("rentFeePerHours", null);
        String depositFee = editor.getString("depositFee", null);
        String plateNumber = editor.getString("plateNumber", null);
        int requireHouseHold = editor.getInt("requireHouseHold", 0);
        int requireIdCard = editor.getInt("requireIdCard", 0);


        String imagePath = editor.getString("picture_path", null);
        String imageVehicle1 = editor.getString("img_vehicle_1", null);
        String imageVehicle2 = editor.getString("img_vehicle_2", null);


        ObjectMapper objectMapper = new ObjectMapper();
        Vehicle_New vehicleObj = new Vehicle_New(frameNumber, Integer.valueOf(userID), vehicleInformationID, description, Float.valueOf(rentFeePerSlot),
                Float.valueOf(rentFeePerDay), Float.valueOf(rentFeePerHours), Float.valueOf(depositFee), plateNumber,
                requireHouseHold, requireIdCard, districtID);

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

            Retrofit test = RetrofitConnect.getClient();
            final VehicleAPI testAPI = test.create(VehicleAPI.class);
            Call<ResponseBody> responseBodyCall = testAPI.createVehicle(data, imagesParts);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupOwnerPolicy.this);
                    builder.setMessage("Chúng tôi sẽ gửi email cho bạn sau khi xác thực tài khoản thành công ! Cảm ơn bạn đã đăng ký VRS");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent it = new Intent(SignupOwnerPolicy.this, LoginActivity.class);
                            startActivity(it);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerPolicy.this, SignupOwnerFour.class);
        startActivity(it);
    }
}
