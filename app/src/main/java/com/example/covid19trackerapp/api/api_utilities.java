package com.example.covid19trackerapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class api_utilities {

    private static Retrofit retrofit=null;

    public static api_Interface getapi_Interface(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(api_Interface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(api_Interface.class);
    }
}
