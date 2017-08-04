package com.yamblz.voltek.weather.data.api.weather;

import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Single<WeatherResponseModel> byCityName(
            @Query("q") String cityName,
            @Query("units") String units
    );

    @GET("weather")
    Single<WeatherResponseModel> byCityId(
            @Query("id") int cityId,
            @Query("units") String units
    );

    @GET("forecast")
    Single<WeatherResponseModel> forecastById(
            @Query("id") int cityId,
            @Query("units") String units
    );

}
