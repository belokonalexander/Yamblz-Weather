package com.yamblz.voltek.weather.data.api.weather.response;


import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("id")
    public int id;

    @SerializedName("main")
    public String main;

    @SerializedName("description")
    public String description;

    @SerializedName("icon")
    public String icon;
}
