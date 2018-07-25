package com.example.manhvdse61952.vrc_android.model.api_interface;


import com.example.manhvdse61952.vrc_android.model.api_model.VehicleInformation;
import com.example.manhvdse61952.vrc_android.model.api_model.VehicleUpdate;
import com.example.manhvdse61952.vrc_android.model.search_model.DetailVehicleItem;
import com.example.manhvdse61952.vrc_android.model.search_model.SearchVehicleItem;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface VehicleAPI {
//    @Headers({"Accept: application/json"})
//    @GET("/vehicleinformation/getVehicleMakerList/")
//    Call<List<String>> getVehicleMarker();
//
//    @Headers({"Accept: application/json"})
//    @GET("/vehicleinformation/getVehicleModelsByMaker/{vehiclemaker}")
//    Call<List<String>> getVehicleModel(@Path("vehiclemaker") String vehiclemaker);

    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getAll")
    Call<List<VehicleInformation>> getAllVehicleInfo();

    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getVehiclesYear/{maker}/{model}")
    Call<List<String>> getVehicleYear(@Path("maker") String vehiclemaker,
                                      @Path("model") String model);

    @Headers({"Accept: application/json"})
    @GET("/vehicleinformation/getVehicleInfo/{maker}/{model}/{year}")
    Call<VehicleInformation> getVehicleInfo(@Path("maker") String vehiclemaker,
                                            @Path("model") String model,
                                            @Path("year") String year);

    @Headers({"Accept: application/json"})
    @GET("/vehicle/getByDistrict/{districtID}")
    Call<List<SearchVehicleItem>> getVehicleByDistrict(@Path("districtID") int districtID);

    @Headers({"Accept: application/json"})
    @GET("/vehicle/getByFrameNumber/{framenumber}")
    Call<DetailVehicleItem> getVehicleByFrameNumber(@Path("framenumber") String framenumber);

    @Headers({"Accept: application/json"})
    @GET("/vehicle/checkFrameNumberExisted/{frame}")
    Call<Boolean> checkDuplicatedFrameNumber(@Path("frame") String frame);

    @Headers({"Accept: application/json"})
    @GET("/vehicle/getVehicleByOwner/{ownerID}")
    Call<List<SearchVehicleItem>> getVehicleByOwnerID(@Path("ownerID") int ownerID);

    @Multipart
    @POST("/vehicle/create")
    Call<ResponseBody> createVehicle(@Part("data") RequestBody data, @Part MultipartBody.Part[] files);

    @Headers({"Accept: application/json"})
    @PUT("/vehicle/update/{vehicleFrameNumber}")
    Call<ResponseBody> updateVehicle(@Path("vehicleFrameNumber") String vehicleFrameNumber,
                                     @Body VehicleUpdate vehicleUpdate);
}