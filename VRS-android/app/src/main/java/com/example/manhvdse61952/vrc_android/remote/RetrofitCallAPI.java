package com.example.manhvdse61952.vrc_android.remote;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.model.Account;
import com.example.manhvdse61952.vrc_android.model.Signup;
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

public class RetrofitCallAPI {

    public void checkLogin(final String username, String password, final Context ctx, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConnect.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<ResponseBody> responseBodyCall = testAPI.login(new Account(username, password));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) {
                    Toast.makeText(ctx, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    Intent it = new Intent(ctx, MainActivity.class);
                    it.putExtra(ImmutableValue.MESSAGE_CODE, username);
                    ctx.startActivity(it);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public void checkExistedUsername(final String username, final String password, final String email, final Context ctx) {
        Retrofit test = RetrofitConnect.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkDuplicated(username);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body().toString().equals("true")) {
                    Toast.makeText(ctx, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                } else {
                    //add value to json object to pass it from SignupAccount activity to SignupUserInfo activity
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        Signup signup = new Signup(username, password, email);
                        String json = objectMapper.writeValueAsString(signup);
                        Intent it = new Intent(ctx, SignupUserInfoActivity.class);
                        it.putExtra(ImmutableValue.MESSAGE_CODE, json);
                        ctx.startActivity(it);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SignupAccount(String imagePath, String receiveValue, final Context ctx, final ProgressDialog progressDialog) {
        Retrofit retrofit = RetrofitConnect.getClient();
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
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage("Chúng tôi sẽ gửi email cho bạn sau khi xác thực tài khoản thành công ! Cảm ơn bạn đã đăng ký VRS");
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent it = new Intent(ctx, LoginActivity.class);
                        ctx.startActivity(it);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
