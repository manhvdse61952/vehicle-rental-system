package com.example.manhvdse61952.vrc_android.model.api_interface;

import com.example.manhvdse61952.vrc_android.model.api_model.ComplainIssue;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractCreate;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractFinish;
import com.example.manhvdse61952.vrc_android.model.api_model.ContractItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContractAPI {
    @Headers({"Accept: application/json"})
    @POST("/contract/create")
    Call<String> createContract(@Body ContractCreate data);

    @Headers({"Accept: application/json"})
    @GET("/contract/conversionVNDToUSD")
    Call<Double> convertUSD(@Query("money") int money);

    @Headers({"Accept: application/json"})
    @GET("/contract/getContractByID/{contractID}")
    Call<ContractItem> findContractByID(@Path("contractID") String contractID);

    @Headers({"Accept: application/json"})
    @GET("/contract/getListContractByOwnerID/{ownerID}")
    Call<List<ContractItem>> findContractByOwnerID(@Path("ownerID") int ownerID);

    @Headers({"Accept: application/json"})
    @GET("/contract/getListContractByCustomerID/{customerID}")
    Call<List<ContractItem>> findContractByCustomerID(@Path("customerID") int customerID);

    @Headers({"Accept: application/json"})
    @POST("/contract/returnVehicle")
    Call<ContractFinish> returnVehicle(@Query("contractID") String contractID,
                                        @Query("returnTime") long returnTime);

    @Headers({"Accept: application/json"})
    @POST("/contract/preFinish")
    Call<ResponseBody> preFinishContract(@Query("contractID") int contractID,
                                  @Query("overTime") String overTime,
                                  @Query("inside") String inside,
                                  @Query("outside") String outside);

    @Headers({"Accept: application/json"})
    @POST("/contract/finished/{contractID}")
    Call<ResponseBody> finishContract(@Path("contractID") int contractID);

    @Headers({"Accept: application/json"})
    @POST("/contract/issueContract/{contractID}")
    Call<ResponseBody> issueContract(@Path("contractID") int contractID,
                                     @Body ComplainIssue complainIssue);

    @Headers({"Accept: application/json"})
    @POST("/contract/issueChangeFee/{contractID}")
    Call<ResponseBody> issueChangeFee(@Path("contractID") int contractID,
                                      @Query("overTime") String overTime,
                                      @Query("inside") String inside,
                                      @Query("outside") String outside);

    @Headers({"Accept: application/json"})
    @POST("/contract/refund")
    Call<ResponseBody> removeContract(@Query("contractID") int contractID);

}
