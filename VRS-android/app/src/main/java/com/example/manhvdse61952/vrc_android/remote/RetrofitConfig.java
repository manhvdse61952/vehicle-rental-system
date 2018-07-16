package com.example.manhvdse61952.vrc_android.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static Retrofit retrofit = null;
    static String offlineTest = "http://192.168.43.39:8080";
    static String onlineTest = "https://vrcapi.azurewebsites.net/";
    static String onlineTest2 = "http://103.90.224.144:8080";

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(onlineTest2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
