package com.yamblz.voltek.weather.data.api.weather.models.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 06.08.2017.
 */

public class ForecastResponseModel {


    @SerializedName("city")
    public City city;

    @SerializedName("list")
    public List<ForecastWeather> weather;

}
