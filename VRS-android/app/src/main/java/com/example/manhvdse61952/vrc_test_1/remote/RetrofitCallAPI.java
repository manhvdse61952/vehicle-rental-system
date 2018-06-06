package com.example.manhvdse61952.vrc_test_1.remote;

import android.content.Intent;
import android.widget.Toast;

import com.example.manhvdse61952.vrc_test_1.interfaceAPI.AccountITF;
import com.example.manhvdse61952.vrc_test_1.layout.login.LoginActivity;
import com.example.manhvdse61952.vrc_test_1.layout.main.MainActivity;
import com.example.manhvdse61952.vrc_test_1.model.AccountObj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitCallAPI {
    int result = 0;

    public int checkLogin(String username, String password){
        Retrofit test = RetrofitConnect.getClient();
        final AccountITF testAPI = test.create(AccountITF.class);
        Call<ResponseBody> responseBodyCall = testAPI.checkLogin(new AccountObj(username, password));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null){
                    result = 0;
                }
                else{
                    result = 1;
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result = -1;
            }
        });
        return result;
    }


}
