package com.example.manhvdse61952.vrc_android.model.api_interface;

import com.example.manhvdse61952.vrc_android.model.api_model.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface AddressAPI {
    @Headers({"Accept: application/json"})
    @GET("/district")
    Call<List<City>> getDistrict();
}
