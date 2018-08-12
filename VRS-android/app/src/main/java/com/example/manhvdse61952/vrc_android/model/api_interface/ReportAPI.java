package com.example.manhvdse61952.vrc_android.model.api_interface;

import com.example.manhvdse61952.vrc_android.model.api_model.Report;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ReportAPI {
    @Headers({"Accept: application/json"})
    @GET("/report/viewReportOwnerByMonth/{userID}/{year}/{month}")
    Call<Report> getReportPerMonth(@Path("userID") int userID,
                                   @Path("year") int year,
                                   @Path("month") int month);

    @Headers({"Accept: application/json"})
    @GET("/report/viewReportOwnerByYear/{userID}/{year}")
    Call<Report> getReportPerYear(@Path("userID") int userID,
                                   @Path("year") int year);
}
