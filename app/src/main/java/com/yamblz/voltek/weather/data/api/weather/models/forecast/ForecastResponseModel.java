package com.yamblz.voltek.weather.data.api.weather.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 06.08.2017.
 */

public class ForecastResponseModel {

    @SerializedName("city")
    public final City city;

    @SerializedName("list")
    public final List<ForecastWeather> weather;

    public ForecastResponseModel(City city, List<ForecastWeather> weather) {
        this.city = city;
        this.weather = weather;
    }
}
