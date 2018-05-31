package com.example.manhvdse61952.vrc_test_1.interfaceAPI;

import com.example.manhvdse61952.vrc_test_1.model.SignupObj;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CheckDuplicatedUsername {
    @Headers({"Accept: application/json"})
    @GET("/api/auth/checkUserNameExist/{userName}")
    Call<Boolean> checkDuplicated(@Path("userName") String username);
}
