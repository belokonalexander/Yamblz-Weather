package com.yamblz.voltek.weather.data.api.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<WeatherResponseModel> byCityName(
            @Query("key") String apiKey,
            @Query("q") String cityName
    );

    @GET("weather")
    Call<WeatherResponseModel> byCityId(
            @Query("key") String apiKey,
            @Query("id") int cityId
    );

    @GET("weather")
    Call<WeatherResponseModel> byCoordinates(
            @Query("key") String apiKey,
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );

    @GET("weather")
    Call<WeatherResponseModel> byZipCode(
            @Query("key") String apiKey,
            @Query("zip") String zipCode // e.g. 94040,us
    );
}
