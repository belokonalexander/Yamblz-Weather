package com.yamblz.voltek.weather.data.api.weather.models;


import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    public final int id;

    @SerializedName("main")
    public final String main;

    @SerializedName("description")
    public final String description;

    @SerializedName("icon")
    public final String icon;

    public Weather(int id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }
}
