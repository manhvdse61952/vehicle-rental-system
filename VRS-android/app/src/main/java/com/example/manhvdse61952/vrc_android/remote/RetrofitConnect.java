package com.example.manhvdse61952.vrc_android.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnect {

    private static Retrofit retrofit = null;
    static String offlineTest = "http://172.16.6.78:8080";
    static String onlineTest = "https://vrcapi.azurewebsites.net/";
    static String onlineTest2 = "http://103.90.224.144:8080";

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(offlineTest)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
