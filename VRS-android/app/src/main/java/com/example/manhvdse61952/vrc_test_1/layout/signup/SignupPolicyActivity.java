package com.example.manhvdse61952.vrc_test_1.layout.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_test_1.R;
import com.example.manhvdse61952.vrc_test_1.interfaceAPI.SignupITF;
import com.example.manhvdse61952.vrc_test_1.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_test_1.model.SignupObj;
import com.example.manhvdse61952.vrc_test_1.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_test_1.remote.RetrofitConnect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupPolicyActivity extends AppCompatActivity {

    //private String MESSAGE_CODE = "SignupRoleToSignupPolicy";
    private String receiveValue = "";
    SignupObj signupObj = new SignupObj();
    Button btnAccept;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_policy);

        btnAccept = (Button)findViewById(R.id.btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receiveIt = getIntent();
                receiveValue = receiveIt.getStringExtra(ImmutableValue.MESSAGE_CODE);
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    signupObj = objectMapper.readValue(receiveValue, SignupObj.class);

                    Retrofit test = RetrofitConnect.getClient();
                    final SignupITF testAPI = test.create(SignupITF.class);
                    Call<ResponseBody> responseBodyCall = testAPI.checkSignup(signupObj);
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Intent it = new Intent(SignupPolicyActivity.this, MainActivity.class);
                            startActivity(it);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(SignupPolicyActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                    });

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
