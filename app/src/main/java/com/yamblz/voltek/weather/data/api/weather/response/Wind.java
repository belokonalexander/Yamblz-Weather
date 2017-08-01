package com.yamblz.voltek.weather.data.api.weather.response;

import com.google.gson.annotations.SerializedName;

public class Wind {

    @SerializedName("speed")
    public double speed;

    @SerializedName("deg")
    public double deg;
}
