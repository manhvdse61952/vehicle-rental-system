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
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;
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

    Button btnNext;
    private static final String TAG = SignupOwnerTwo.class.getSimpleName();
    Signup signupObj = new Signup();
    String receiveValue = "", imagePath = "";
    ProgressDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_owner_four);

        Intent receiveIt = getIntent();
        receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
        imagePath = receiveIt.getStringExtra("PICTURE_FILE_PATH");

        btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Signup for customer
                dialog = ProgressDialog.show(SignupOwnerFour.this, "Đăng ký chủ xe",
                        "Đang xử lý ...", true);
                RetrofitCallAPI rfCall = new RetrofitCallAPI();
                rfCall.SignupAccount(imagePath, receiveValue, SignupOwnerFour.this, dialog);

                SharedPreferences editor = getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                createVehicle(editor.getString("user-id", "37"));


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it = new Intent(SignupOwnerFour.this, SignupOwnerThree.class);
        startActivity(it);
    }

    public void createVehicle(String userID){
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

        String seat = editor.getString("seat", null);
        ObjectMapper objectMapper = new ObjectMapper();
        VehiclePayload vehiclePayload = new VehiclePayload(frameNumber,Integer.parseInt(userID), null, Float.valueOf(rent),Float.valueOf(deposite),"", vehicleName,Integer.valueOf(seat), Integer.valueOf(year),engine,transmission);
        try {
            String json = objectMapper.writeValueAsString(vehiclePayload);

            String IMG_JPEG = "image/jpeg";
            File imageFile = new File(imagePath);
            RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);
            RequestBody data = RequestBody.create(MediaType.parse("text/plain"), json);
            MultipartBody.Part body = MultipartBody.Part.createFormData("files", imageFile.getName(), fileBody);
            MultipartBody.Part[] imagesParts = new MultipartBody.Part[1];
            imagesParts[0] = body;
            Retrofit test = RetrofitConnect.getClient();
            final AccountAPI testAPI = test.create(AccountAPI.class);
            Call<ResponseBody> responseBodyCall = testAPI.createVehicle(data,imagesParts);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    JSONObject jObjError = null;
                    JSONObject testObj = null;
                    try {
                        testObj = new JSONObject(response.body().string());
                        jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(SignupOwnerFour.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(SignupOwnerFour.this, response.errorBody().toString()+ response.body().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(SignupOwnerFour.this, "NAHHHHHHHH", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
