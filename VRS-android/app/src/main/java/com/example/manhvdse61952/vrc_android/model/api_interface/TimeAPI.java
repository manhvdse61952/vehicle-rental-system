package com.example.manhvdse61952.vrc_android.model.api_interface;

import com.example.manhvdse61952.vrc_android.model.api_model.RentTime;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface TimeAPI {
    @Headers({"Accept: application/json"})
    @GET("/contract/getBusyTimeVehicle/{vehicleID}")
    Call<List<RentTime>> getBusyTimeByVehicleID(@Path("vehicleID") String vehicleID);

    @Headers({"Accept: application/json"})
    @GET("/contract/getCurrentServerTime")
    Call<Long> getServerTime();
}
