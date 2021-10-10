package com.example.covid19trackerapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("top-headlines")
    Call<News> getNews(
            @Query("country") String country,
            @Query("category") String category,
            @Query("apikey") String apikey
    );
    @GET("everything")
    Call<News> getNews(
            @Query("q") String keyword,
            @Query("country") String country,
            @Query("category") String category,
            @Query("sortBy") String sortBy,
            @Query("apiKey") String apiKey
    );
}
