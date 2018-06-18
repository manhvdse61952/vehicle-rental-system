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
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupPolicyActivity;
import com.example.manhvdse61952.vrc_android.model.Signup;
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
        btnSignupAccept = (Button)findViewById(R.id.btnSignupAccept);
        btnSignupBack = (Button)findViewById(R.id.btnSignupBack);
        cbxSignupPolicy = (CheckBox)findViewById(R.id.cbxSignupPolicy);

        //Checkbox
        cbxSignupPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
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
                String address = editor.getString("address", null);
                String CMND_image_path = editor.getString("CMND_image_path", null);
                String rolename = editor.getString("rolename", null);

                //call api
                ObjectMapper objectMapper = new ObjectMapper();
                Signup signupObj = new Signup(address, email, cmnd, CMND_image_path, name, password, paypal, phone, rolename, username);
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
                            } catch(Exception e) {

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

    public void createVehicle(String userID){

        //Get vehicle value from shared preferences
        SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        String frameNumber = editor.getString("frameNumber",null);
        String rent = editor.getString("rent",null);
        String deposite = editor.getString("deposit",null);
        int householdRequired = editor.getInt("household_registration",0);
        int vehicleRequired = editor.getInt("vehicle_registration", 0);
        int idRequired = editor.getInt("id_card", 0);
        String vehicleName = editor.getString("vehicleName", null);
        String year = editor.getString("vehicleYear", null);
        String engine = editor.getString("engine", null);
        String transmission = editor.getString("tranmission", null);
        String imagePath = editor.getString("picture_path",  null);
        String imageVehicle1 = editor.getString("img_vehicle_1", null);
        String imageVehicle2 = editor.getString("img_vehicle_2", null);
//        String imageVehicle3 = editor.getString("img_vehicle_3", null);
//        String imageVehicle4 = editor.getString("img_vehicle_4", null);
//        String imageVehicle5 = editor.getString("img_vehicle_5", null);
//        String imageVehicle6 = editor.getString("img_vehicle_6", null);

        String seat = editor.getString("seat", null);
        ObjectMapper objectMapper = new ObjectMapper();
        VehiclePayload vehiclePayload = new VehiclePayload(frameNumber,Integer.valueOf(userID), null, Float.valueOf(rent),Float.valueOf(deposite),"", vehicleName,Integer.valueOf(seat), Integer.valueOf(year),engine,transmission);
        try {
            String json = objectMapper.writeValueAsString(vehiclePayload);
            String IMG_JPEG = "image/jpeg";
            File imageFile = new File(imagePath);
            File imageVehicleFile1 = new File(imageVehicle1);
            File imageVehicleFile2 = new File(imageVehicle2);
//            File imageVehicleFile3 = new File(imageVehicle3);
//            File imageVehicleFile4 = new File(imageVehicle4);
//            File imageVehicleFile5 = new File(imageVehicle5);
//            File imageVehicleFile6 = new File(imageVehicle6);
            RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);
            RequestBody fileBody2 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile1);
            RequestBody fileBody3 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile2);
//            RequestBody fileBody4 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile3);
//            RequestBody fileBody5 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile4);
//            RequestBody fileBody6 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile5);
//            RequestBody fileBody7 = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageVehicleFile6);

            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), json);

            MultipartBody.Part body = MultipartBody.Part.createFormData("files", imageFile.getName(), fileBody);
            MultipartBody.Part body2 = MultipartBody.Part.createFormData("files", imageVehicleFile1.getName(), fileBody2);
            MultipartBody.Part body3 = MultipartBody.Part.createFormData("files", imageVehicleFile2.getName(), fileBody3);
//            MultipartBody.Part body4 = MultipartBody.Part.createFormData("files", imageVehicleFile3.getName(), fileBody4);
//            MultipartBody.Part body5 = MultipartBody.Part.createFormData("files", imageVehicleFile4.getName(), fileBody5);
//            MultipartBody.Part body6 = MultipartBody.Part.createFormData("files", imageVehicleFile5.getName(), fileBody6);
//            MultipartBody.Part body7 = MultipartBody.Part.createFormData("files", imageVehicleFile6.getName(), fileBody7);
            MultipartBody.Part[] imagesParts = new MultipartBody.Part[3];
            imagesParts[0] = body;
            imagesParts[1] = body2;
            imagesParts[2] = body3;
//            imagesParts[3] = body4;
//            imagesParts[4] = body5;
//            imagesParts[5] = body6;
//            imagesParts[6] = body7;
            Retrofit test = RetrofitConnect.getClient();
            final AccountAPI testAPI = test.create(AccountAPI.class);
            Call<ResponseBody> responseBodyCall = testAPI.createVehicle(data,imagesParts);
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