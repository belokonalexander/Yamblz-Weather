package com.yamblz.voltek.weather.data.api.weather.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponseModel {

    @SerializedName("weather")
    public List<Weather> weather = null;

    @SerializedName("main")
    public Main main;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

}
