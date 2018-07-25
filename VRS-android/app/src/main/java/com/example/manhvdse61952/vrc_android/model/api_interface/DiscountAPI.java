package com.example.manhvdse61952.vrc_android.model.api_interface;

import com.example.manhvdse61952.vrc_android.model.api_model.Discount;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DiscountAPI {
    @Headers({"Accept: application/json"})
    @GET("/discount/listDiscount/{ownerID}")
    Call<List<Discount>> getListPromotion(@Path("ownerID") int ownerID);

    @Headers({"Accept: application/json"})
    @POST("/discount/updateDiscountByVehicle/{frameNumber}")
    Call<ResponseBody> updatePromotion(@Path("frameNumber") String frameNumber,
                                       @Query("percent") float percent,
                                       @Query("startDate") long startDate,
                                       @Query("endDate") long endDate);

    @Headers({"Accept: application/json"})
    @GET("/discount/removeDiscount/{frameNumber}")
    Call<ResponseBody> removePromotion(@Path("frameNumber") String frameNumber);

}
