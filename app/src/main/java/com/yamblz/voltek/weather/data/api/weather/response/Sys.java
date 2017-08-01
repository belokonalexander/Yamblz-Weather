package com.yamblz.voltek.weather.data.api.weather.response;

import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("country")
    public String country;

    @SerializedName("sunrise")
    public int sunrise;

    @SerializedName("sunset")
    public int sunset;
}
