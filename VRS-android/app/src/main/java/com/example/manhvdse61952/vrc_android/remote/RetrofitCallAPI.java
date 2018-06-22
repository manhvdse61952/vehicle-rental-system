package com.example.manhvdse61952.vrc_android.remote;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.api.AddressAPI;
import com.example.manhvdse61952.vrc_android.api.VehicleAPI;
import com.example.manhvdse61952.vrc_android.layout.main.activity_main_2;
import com.example.manhvdse61952.vrc_android.api.AccountAPI;
import com.example.manhvdse61952.vrc_android.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupRoleActivity;
import com.example.manhvdse61952.vrc_android.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.model.apiModel.City;
import com.example.manhvdse61952.vrc_android.model.apiModel.Login;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitCallAPI {
    public static List<City> lisCityTest = new ArrayList<>();
    public static List<String> vehicleMaker = new ArrayList<>();
    public static List<String> vehicleModel = new ArrayList<>();
    public static List<String> vehicleYear = new ArrayList<>();

    /// Check login////
    public void checkLogin(final String username, String password, final Context ctx, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConnect.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<ResponseBody> responseBodyCall = testAPI.login(new Login(username, password));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    Toast.makeText(ctx, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 403) {
                    Toast.makeText(ctx, "Tài khoản chưa được chấp nhận! Vui lòng quay lại sau", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                    editor.putString("usernameAfterLogin", username);
                    editor.apply();
                    Intent it = new Intent(ctx, activity_main_2.class);
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

    /// Check duplicated username ///
    public void checkExistedUsername(final String username, final String password,
                                     final String email, final Context ctx, final EditText edt1, final EditText edt2, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConnect.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkDuplicated(username);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body().toString().equals("true")) {
                    progressDialog.dismiss();
                    edt1.setError("Tài khoản đã có người sử dụng");
                } else {
                    checkExistedEmail(username, email, password, ctx, edt2, progressDialog);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /// Check duplicated email ///
    public void checkExistedEmail(final String username, final String email,
                                  final String password, final Context ctx, final EditText edt, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConnect.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkEmail(email);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body().toString().equals("true")) {
                    edt.setError("Email đã có người sử dụng");
                } else {
                    SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.putString("email", email);
                    editor.apply();

                    Intent it = new Intent(ctx, SignupUserInfoActivity.class);
                    ctx.startActivity(it);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /// Check duplicated CMND ///
    public void checkExistedCmnd(final String cmnd, final String name, final String phone,
                                 final String imagePath, final Context ctx, final EditText input, final ProgressDialog progressDialog) {
        Retrofit test = RetrofitConnect.getClient();
        final AccountAPI testAPI = test.create(AccountAPI.class);
        Call<Boolean> responseBodyCall = testAPI.checkCmnd(cmnd);
        responseBodyCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body().toString().equals("true")) {
                    input.setError("CMND đã có người sử dụng");
                } else {
                    SharedPreferences.Editor editor = ctx.getSharedPreferences(ImmutableValue.SHARED_PREFERENCES_CODE, ctx.MODE_PRIVATE).edit();
                    editor.putString("name", name);
                    editor.putString("phone", phone);
                    editor.putString("cmnd", cmnd);
                    editor.putString("paypal", "default");
                    editor.putString("CMND_image_path", imagePath);
                    editor.apply();

                    Intent it = new Intent(ctx, SignupRoleActivity.class);
                    ctx.startActivity(it);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /// Get all district and city in database ///
    public List<City> getAllAddress() {
        lisCityTest = new ArrayList<>();
        Retrofit test = RetrofitConnect.getClient();
        final AddressAPI testAPI = test.create(AddressAPI.class);
        Call<List<City>> responseBodyCall = testAPI.getDistrict();

        responseBodyCall.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                lisCityTest = response.body();
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
        return lisCityTest;
    }

    /// Get all vehicle maker  ///
    public List<String> getAllVehicleMaker() {
        vehicleMaker = new ArrayList<>();
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleMarker();

        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        vehicleMaker.add(response.body().get(i).toString());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        return vehicleMaker;
    }

    /// get all vehicle model by maker ///
    public List<String> getAllVehicleModel(String maker) {
        vehicleModel = new ArrayList<>();
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleModel(maker);

        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        vehicleModel.add(response.body().get(i).toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        return vehicleModel;
    }

    /// get all vehicle year by maker and model ///
    public List<String> getAllYear(String maker, String model) {
        vehicleYear = new ArrayList<>();
        Retrofit test = RetrofitConnect.getClient();
        final VehicleAPI testAPI = test.create(VehicleAPI.class);
        Call<List<String>> responseBodyCall = testAPI.getVehicleYear(maker, model);

        responseBodyCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        vehicleYear.add(response.body().get(i).toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
        return vehicleYear;
    }

    /// Signup customer account
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
                alertDialog.setCanceledOnTouchOutside(false);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
