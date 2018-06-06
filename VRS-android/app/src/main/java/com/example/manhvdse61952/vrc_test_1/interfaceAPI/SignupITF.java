package com.example.manhvdse61952.vrc_test_1.interfaceAPI;

import com.example.manhvdse61952.vrc_test_1.model.AccountObj;
import com.example.manhvdse61952.vrc_test_1.model.SignupObj;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SignupITF {
    @Headers({"Content-Type: multipart/form-data"})
    @POST("api/auth/signupwithimage")
    Call<ResponseBody> checkSignup(@Field("data") String data, @Field("file") File file);
}
