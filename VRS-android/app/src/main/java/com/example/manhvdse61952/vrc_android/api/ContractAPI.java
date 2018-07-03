package com.example.manhvdse61952.vrc_android.api;

import com.example.manhvdse61952.vrc_android.model.apiModel.ContractCreate;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ContractAPI {
    @Headers({"Accept: application/json"})
    @POST("/contract/create")
    Call<String> createContract(@Body ContractCreate data);

    @Headers({"Accept: application/json"})
    @GET("/contract/conversionVNDToUSD")
    Call<Double> convertUSD(@Query("money") int money);

}
