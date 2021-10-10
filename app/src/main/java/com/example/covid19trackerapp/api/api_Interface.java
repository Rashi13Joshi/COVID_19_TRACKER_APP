package com.example.covid19trackerapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface api_Interface {

    static final String BASE_URL="https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<CountryData>> getCountryData();
}
