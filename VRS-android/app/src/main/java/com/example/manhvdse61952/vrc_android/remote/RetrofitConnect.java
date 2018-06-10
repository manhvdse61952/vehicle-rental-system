package com.example.manhvdse61952.vrc_android.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnect {

    private static Retrofit retrofit = null;
    static String offlineTest = "http://192.168.0.100:8080";
    static String onlineTest = "https://vrcapi.azurewebsites.net/";
    static String offlineTest2 = "http://103.90.224.144:8080";
    static String offlineTest3 = "http://192.168.100.139:8080";
    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(offlineTest2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
