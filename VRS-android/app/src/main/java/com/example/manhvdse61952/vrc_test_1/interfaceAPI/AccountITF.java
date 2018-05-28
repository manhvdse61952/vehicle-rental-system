package com.example.manhvdse61952.vrc_test_1.interfaceAPI;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AccountITF {
    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> checkLogin(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("role") int role);
}
