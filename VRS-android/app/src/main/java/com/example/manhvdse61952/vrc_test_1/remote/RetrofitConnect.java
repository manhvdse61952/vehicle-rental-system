package com.example.manhvdse61952.vrc_test_1.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConnect {

    private static Retrofit retrofit = null;
    //http://192.168.43.39:8080/
    //https://vrcapi.azurewebsites.net/
    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.43.39:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
