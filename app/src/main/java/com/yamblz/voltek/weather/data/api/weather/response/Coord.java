package com.yamblz.voltek.weather.data.api.weather.response;

import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    public double lon;

    @SerializedName("lat")
    public double lat;
}
