package com.example.manhvdse61952.vrc_android.controller.layout.signup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.R;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.controller.validate.ValidateInput;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupAccountActivity extends AppCompatActivity {

    Button btnSignupAccountClean, btnNext;
    EditText edtSignupUsername, edtSignupPassword, edtSignupEmail;
    TextInputLayout signup_username_txt, signup_password_txt, signup_email_txt;
    private String username = "", password = "", email = "";
    ValidateInput validObj;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        declareID();

        //button Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextAction();
            }
        });

        //Back clean
        btnSignupAccountClean = (Button) findViewById(R.id.btnSignupAccountClean);
        btnSignupAccountClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSignupUsername.setText("");
                edtSignupEmail.setText("");
                edtSignupPassword.setText("");
            }
        });

    }

    @Override
    public void onBackPressed() {
        SharedPreferences settings_2 = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
        settings_2.edit().clear().commit();
        SignupAccountActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void nextAction(){
        //get value from edittext
        username = edtSignupUsername.getText().toString().trim();
        password = edtSignupPassword.getText().toString().trim();
        email = edtSignupEmail.getText().toString().trim();

        validObj = new ValidateInput();
        Boolean checkUsername = validObj.validUsername(username, edtSignupUsername);
        Boolean checkPassword = validObj.validPassword(password, edtSignupPassword);
        Boolean checkEmail = validObj.validEmail(email, edtSignupEmail);
        if (checkUsername && checkPassword && checkEmail) {
            //Call API
            dialog = ProgressDialog.show(SignupAccountActivity.this, "Đăng ký",
                    "Đang kiểm tra ...", true);
            checkExistedUsername(username, password, email, SignupAccountActivity.this, edtSignupUsername, edtSignupEmail, dialog);

        }
    }

    private void checkExistedUsername(final String username, final String password,
                                     final String email, final Context ctx, final EditText edt1, final EditText edt2, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConfig.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkDuplicated(username);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200){
                    if (response.body().toString().equals("true")) {
                        progressDialog.dismiss();
                        edt1.setError("Tài khoản đã có người sử dụng");
                        edt1.requestFocus();
                    } else {
                        checkExistedEmail(username, email, password, ctx, edt2, progressDialog);
                    }
                } else {
                    Toast.makeText(ctx, "Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkExistedEmail(final String username, final String email,
                                  final String password, final Context ctx, final EditText edt, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConfig.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkEmail(email);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 200){
                    if (response.body().toString().equals("true")) {
                        edt.setError("Email đã có người sử dụng");
                        edt.requestFocus();
                    } else {
                        SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                        editor.putString(ImmutableValue.SIGNUP_username, username);
                        editor.putString(ImmutableValue.SIGNUP_password, password);
                        editor.putString(ImmutableValue.SIGNUP_email, email);
                        editor.apply();

                        Intent it = new Intent(ctx, SignupUserInfoActivity.class);
                        ctx.startActivity(it);
                    }
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(ctx, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void declareID(){
        //Declare id
        edtSignupUsername = (EditText) findViewById(R.id.edtSignupUsername);
        edtSignupPassword = (EditText) findViewById(R.id.edtSignupPassword);
        edtSignupEmail = (EditText) findViewById(R.id.edtSignupEmail);
        signup_username_txt = (TextInputLayout) findViewById(R.id.signup_username_txt);
        signup_password_txt = (TextInputLayout) findViewById(R.id.signup_password_txt);
        signup_email_txt = (TextInputLayout) findViewById(R.id.signup_email_txt);
        btnNext = (Button) findViewById(R.id.btnSignupAccountNext);
    }

}
