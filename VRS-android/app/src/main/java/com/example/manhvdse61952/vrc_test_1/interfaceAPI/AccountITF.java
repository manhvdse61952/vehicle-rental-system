package com.example.manhvdse61952.vrc_test_1.interfaceAPI;

import com.example.manhvdse61952.vrc_test_1.model.AccountObj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AccountITF {
    @Headers({"Accept: application/json"})
    @POST("api/auth/signin")
    Call<ResponseBody> checkLogin(@Body AccountObj accountObj);
}