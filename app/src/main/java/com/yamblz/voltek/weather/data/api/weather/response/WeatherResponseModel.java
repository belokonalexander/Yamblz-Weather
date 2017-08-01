package com.yamblz.voltek.weather.data.api.weather.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponseModel {

    @SerializedName("coord")
    public Coord coord;

    @SerializedName("sys")
    public Sys sys;

    @SerializedName("weather")
    public List<Weather> weather = null;

    @SerializedName("main")
    public Main main;

    @SerializedName("wind")
    public Wind wind;

    @SerializedName("rain")
    public Rain rain;

    @SerializedName("clouds")
    public Clouds clouds;

    @SerializedName("dt")
    public int dt;

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("cod")
    public int cod;
}
