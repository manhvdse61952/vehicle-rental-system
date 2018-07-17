package com.example.manhvdse61952.vrc_android.controller.resources;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_android.controller.permission.PermissionDevice;
import com.example.manhvdse61952.vrc_android.model.api_interface.AddressAPI;
import com.example.manhvdse61952.vrc_android.model.api_interface.VehicleAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_android.model.api_interface.AccountAPI;
import com.example.manhvdse61952.vrc_android.controller.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.signup.customer.SignupRoleActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.signup.customer.SignupUserInfoActivity;
import com.example.manhvdse61952.vrc_android.controller.layout.signup.owner.SignupOwnerPolicy;
import com.example.manhvdse61952.vrc_android.model.api_model.Account;
import com.example.manhvdse61952.vrc_android.model.api_model.City;
import com.example.manhvdse61952.vrc_android.model.api_model.Login;
import com.example.manhvdse61952.vrc_android.remote.RetrofitConfig;

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

public class GeneralAPI {
    public static List<City> listAddressFromDB = new ArrayList<>();
    /// Get all district and city in database ///
    public void getAllAddress(final ProgressDialog progressDialog, final Context ctx) {
        listAddressFromDB = new ArrayList<>();
        Retrofit test = RetrofitConfig.getClient();
        final AddressAPI testAPI = test.create(AddressAPI.class);
        Call<List<City>> responseBodyCall = testAPI.getDistrict();

        responseBodyCall.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if (response.code() == 200){
                    listAddressFromDB = response.body();
                } else {
                    Toast.makeText(ctx, "Đã xảy ra lỗi! Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {
                Toast.makeText(ctx, "Kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
