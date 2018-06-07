package com.example.manhvdse61952.vrc_android.layout.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.SignupAccountActivity;
import com.example.manhvdse61952.vrc_android.model.Account;
import com.example.manhvdse61952.vrc_android.remote.ImmutableValue;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConnect;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    TextView txtSignUp;
    Button btnLogin;
    EditText edtUsername, edtPassword;
    String username = "";
    String password = "";

    //public final static String MESSAGE_KEY = "loginActivity.to.mainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Login button
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = edtUsername.getText().toString();
                password = edtPassword.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    Retrofit test = RetrofitConnect.getClient();
                    final AccountAPI testAPI = test.create(AccountAPI.class);
                    Call<ResponseBody> responseBodyCall = testAPI.login(new Account(username, password));
                    responseBodyCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.body() == null){
                                Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                it.putExtra(ImmutableValue.MESSAGE_CODE, username);
                                startActivity(it);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        //Sign up link
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(LoginActivity.this, SignupAccountActivity.class);
                startActivity(it);
            }
        });


    }
}
