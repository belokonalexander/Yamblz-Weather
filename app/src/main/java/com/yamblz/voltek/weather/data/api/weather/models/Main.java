package com.yamblz.voltek.weather.data.api.weather.models;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    public double temp;

    @SerializedName("humidity")
    public double humidity;

    @SerializedName("pressure")
    public double pressure;

    @SerializedName("temp_min")
    public double tempMin;

    @SerializedName("temp_max")
    public double tempMax;
}
