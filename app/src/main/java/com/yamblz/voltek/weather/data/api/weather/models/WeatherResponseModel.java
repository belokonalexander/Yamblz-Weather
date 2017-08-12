package com.yamblz.voltek.weather.data.api.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponseModel {

    @SerializedName("weather")
    public List<Weather> weather = null;

    @SerializedName("main")
    public final Main main;

    @SerializedName("id")
    public final int id;

    @SerializedName("name")
    public final String name;

    public WeatherResponseModel(Main main, int id, String name) {
        this.main = main;
        this.id = id;
        this.name = name;
    }
}
