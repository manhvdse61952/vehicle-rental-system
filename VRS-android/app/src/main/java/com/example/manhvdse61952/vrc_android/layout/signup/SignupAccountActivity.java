package com.example.manhvdse61952.vrc_android.layout.signup;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.model.Signup;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupAccountActivity extends AppCompatActivity {

    Button btnBack, btnNext;
    EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;
    private String username = "", password = "", email = "";

    //private String MESSAGE_CODE = "SignupAccountActivityToSignupInfoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);

        //Edit text
        edtSignupUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);


        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Use for test
                Intent it = new Intent(SignupAccountActivity.this, SignupUserInfoActivity.class);
                ObjectMapper objectMapper = new ObjectMapper();
                Signup signup = new Signup(username, password, email);
                try {
                    String json = objectMapper.writeValueAsString(signup);
                    it.putExtra(ImmutableValue.MESSAGE_CODE, json);
                    startActivity(it);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                //////////////////////////////////////////////////

//                username = edtSignupUsername.getText().toString();
//                password = edtSignupPassword.getText().toString();
//                email = edtSignupEmail.getText().toString();
//                if (username.length() == 0){
//                    edtSignupUsername.setError("username is required!");
//                }
//                else if (password.length() == 0){
//                    edtSignupPassword.setError("password is required!");
//                } else if (email.length() == 0){
//                    edtSignupEmail.setError("email is required!");
//                } else {
//                    Retrofit test = RetrofitConnect.getClient();
//                    final CheckDuplicatedUsername testAPI = test.create(CheckDuplicatedUsername.class);
//                    Call<Boolean> responseBodyCall = testAPI.checkDuplicated(username);
//                    responseBodyCall.enqueue(new Callback<Boolean>() {
//                        @Override
//                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                            if (response.body().toString().equals("true")) {
//                                Toast.makeText(SignupAccountActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Intent it = new Intent(SignupAccountActivity.this, SignupUserInfoActivity.class);
//                                ObjectMapper objectMapper = new ObjectMapper();
//                                Signup signup = new Signup(username, password, email);
//                                try {
//                                    String json = objectMapper.writeValueAsString(signup);
//                                    it.putExtra(ImmutableValue.MESSAGE_CODE, json);
//                                    startActivity(it);
//                                } catch (JsonProcessingException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<Boolean> call, Throwable t) {
//                            Toast.makeText(SignupAccountActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }


            }
        });

        btnBack = (Button) findViewById(R.id.btnSignupAccountBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent it2 = new Intent(SignupAccountActivity.this, LoginActivity.class);
//                startActivity(it2);
                try {


                    Retrofit retrofit = RetrofitConnect.getClient();
                    final AccountAPI accountAPI = retrofit.create(AccountAPI.class);
                    AssetManager mgr = getAssets();

                    InputStream fin =  mgr.open("images/xe_oto.jpg");
                    File file = File.createTempFile("xeoto","jpg");
                    OutputStream outStream = new FileOutputStream(file);
                    byte[] buffer = new byte[fin.available()];
                    fin.read(buffer);
                    outStream.write(buffer);
                    outStream.close();
                    String IMG_JPEG = "image/jpeg";
                    RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), file);

                    String receiveValue = "{\"username\":\"tester12\", \"password\":\"master\", \"email\":\"pos2132@gmail.com\", \"rolename\":\"ROLE_EMPLOYEE\", \"name\":\"ROLE_EMPLOYEE\"}";
                    RequestBody data = RequestBody.create(MediaType.parse("text/plain"), receiveValue);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
                    Call<ResponseBody> responseBodyCall = accountAPI.signup(data,body);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.e("RESPONSE SIGNUP", response.message().toString());
                            Toast.makeText(SignupAccountActivity.this
                                    , response.message()+ "OK", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SignupAccountActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it3 = new Intent(SignupAccountActivity.this, LoginActivity.class);
        startActivity(it3);
    }
}
