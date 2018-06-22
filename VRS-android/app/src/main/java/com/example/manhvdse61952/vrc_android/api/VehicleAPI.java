package com.example.manhvdse61952.vrc_android.api;


import com.example.manhvdse61952.vrc_android.model.apiModel.VehicleInformation_New;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getVehicleInfo/{maker}/{model}/{year}")
    Call<VehicleInformation_New> getVehicleInfo(@Path("maker") String vehiclemaker,
                                                @Path("model") String model,
                                                @Path("year") String year);

    @Multipart
    @POST("/vehicle/create")
    Call<ResponseBody> createVehicle(@Part("data") RequestBody data, @Part MultipartBody.Part[] files);
}
