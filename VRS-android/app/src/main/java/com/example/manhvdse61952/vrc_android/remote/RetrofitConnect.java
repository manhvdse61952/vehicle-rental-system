package com.example.manhvdse61952.vrc_android.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnect {

    private static Retrofit retrofit = null;
    static String offlineTest = "http://192.168.0.102:8080";
    static String onlineTest = "https://vrcapi.azurewebsites.net/";
    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(onlineTest)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
