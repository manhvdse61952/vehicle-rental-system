package com.example.manhvdse61952.vrc_android.api;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface VehicleAPI {
    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getVehicleMakerList/")
    Call<List<String>> getVehicleMarker();

    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getVehicleModelsByMaker/{vehiclemaker}")
    Call<List<String>> getVehicleModel(@Path("vehiclemaker") String vehiclemaker);

    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getVehiclesYear/{maker}/{model}")
    Call<List<String>> getVehicleYear(@Path("maker") String vehiclemaker,
                                      @Path("model") String model);


}
