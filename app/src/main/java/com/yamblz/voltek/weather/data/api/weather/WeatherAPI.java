package com.yamblz.voltek.weather.data.api.weather;

import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("weather")
    Single<WeatherResponseModel> byCityName(
            @Query("q") String cityName
    );

    @GET("weather")
    Single<WeatherResponseModel> byCityId(
            @Query("id") int cityId
    );

}
