package com.example.manhvdse61952.vrc_android.api;

import com.example.manhvdse61952.vrc_android.model.apiModel.Account;
import com.example.manhvdse61952.vrc_android.model.apiModel.Login;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AccountAPI {
    @Headers({"Accept: application/json"})
    @POST("api/auth/signin")
    Call<ResponseBody> login(@Body Login loginModel);

    @Headers({"Accept: application/json"})
    @GET("api/auth/checkUserNameExist/{userName}")
    Call<Boolean> checkDuplicated(@Path("userName") String username);

    @Headers({"Accept: application/json"})
    @GET("account/checkEmailExist/{email}")
    Call<Boolean> checkEmail(@Path("email") String email);

    @Headers({"Accept: application/json"})
    @GET("account/checkCmndExist/{cmnd}")
    Call<Boolean> checkCmnd(@Path("cmnd") String cmnd);

    @Multipart
    @POST("api/auth/signupwithimage")
    Call<ResponseBody> signup(@Part("data") RequestBody data, @Part MultipartBody.Part file);

    @Multipart
    @POST("vehicle/create")
    Call<ResponseBody> createVehicle(@Part("data") RequestBody data, @Part MultipartBody.Part[] files);
}
