package com.yamblz.voltek.weather.data.api.weather;

import com.yamblz.voltek.weather.data.api.ApiConst;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Call<WeatherResponseModel> byCityName(
            @Query(ApiConst.API_KEY) String apiKey,
            @Query(ApiConst.CITY_NAME) String cityName
    );

    @GET("weather")
    Call<WeatherResponseModel> byCityId(
            @Query(ApiConst.API_KEY) String apiKey,
            @Query(ApiConst.CITY_ID) int cityId
    );

    @GET("weather")
    Call<WeatherResponseModel> byCoordinates(
            @Query(ApiConst.API_KEY) String apiKey,
            @Query(ApiConst.LATITUDE) double latitude,
            @Query(ApiConst.LONGITUDE) double longitude
    );

    @GET("weather")
    Call<WeatherResponseModel> byZipCode(
            @Query(ApiConst.API_KEY) String apiKey,
            @Query(ApiConst.ZIP_CODE) String zipCode // e.g. 94040,us
    );
}
