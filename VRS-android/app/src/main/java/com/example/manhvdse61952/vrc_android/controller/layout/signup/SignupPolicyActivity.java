package com.example.manhvdse61952.vrc_android.controller.layout.signup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.manhvdse61952.vrc_android.controller.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.controller.resources.ImmutableValue;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.model.api_model.Signup;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupPolicyActivity extends AppCompatActivity {

    Button btnSignupAccountAccept;
    CheckBox cbxSignupPolicy;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_policy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        //Declare id
        btnSignupAccountAccept = (Button)findViewById(R.id.btnSignupAccountAccept);
        cbxSignupPolicy = (CheckBox)findViewById(R.id.cbxSignupPolicy);

        //Checkbox
        cbxSignupPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox)view).isChecked()){
                    btnSignupAccountAccept.setEnabled(true);
                    btnSignupAccountAccept.setBackground(getResources().getDrawable(R.drawable.border_green_primarygreen));
                } else {
                    btnSignupAccountAccept.setEnabled(false);
                    btnSignupAccountAccept.setBackground(getResources().getDrawable(R.drawable.border_green_hide));
                }
            }
        });

        btnSignupAccountAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get value from shared preferences
                SharedPreferences editor = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                String username = editor.getString(ImmutableValue.SIGNUP_username, null);
                String password = editor.getString(ImmutableValue.SIGNUP_password, null);
                String email = editor.getString(ImmutableValue.SIGNUP_email, null);
                String name = editor.getString(ImmutableValue.SIGNUP_fullName, null);
                String phone = editor.getString(ImmutableValue.SIGNUP_phone, null);
                String cmnd = editor.getString(ImmutableValue.SIGNUP_cmnd, null);
                String paypal = "";
                String CMND_image_path = editor.getString(ImmutableValue.SIGNUP_img_CMND, null);
                String rolename = editor.getString(ImmutableValue.SIGNUP_role, null);

                //Call API
                ObjectMapper objectMapper = new ObjectMapper();
                Signup signupObj = new Signup("", email, cmnd, CMND_image_path, name, password, paypal, phone, rolename, username);
                try {
                    String json = objectMapper.writeValueAsString(signupObj);
                    dialog = ProgressDialog.show(SignupPolicyActivity.this, "Đăng ký",
                            "Đang xử lý ...", true);
                    SignupAccount(CMND_image_path, json, SignupPolicyActivity.this, dialog);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        SignupPolicyActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Sign up customer account
    public void SignupAccount(String imagePath, String receiveValue, final Context ctx, final ProgressDialog progressDialog) {
        Retrofit retrofit = RetrofitConfig.getClient();
        final AccountAPI accountAPI = retrofit.create(AccountAPI.class);
        String IMG_JPEG = "image/jpeg";
        File imageFile = new File(imagePath);
        RequestBody fileBody = RequestBody.create(okhttp3.MediaType.parse(IMG_JPEG), imageFile);
        RequestBody data = RequestBody.create(MediaType.parse("text/plain"), receiveValue);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), fileBody);
        Call<ResponseBody> responseBodyCall = accountAPI.signup(data, body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    builder.setMessage("Chúng tôi sẽ gửi email cho bạn sau khi xác thực tài khoản thành công, thời gian chỉ từ 5-10 phút hoặc sớm hơn ! Cảm ơn bạn đã đăng ký VRS");
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences settings = getSharedPreferences(ImmutableValue.SIGNUP_SHARED_PREFERENCES_CODE, MODE_PRIVATE);
                            settings.edit().clear().commit();
                            SignupPolicyActivity.this.finish();
                            Intent it = new Intent(ctx, LoginActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(it);
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.setCanceledOnTouchOutside(false);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ctx, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
