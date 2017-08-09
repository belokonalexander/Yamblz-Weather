package com.yamblz.voltek.weather.data.api.weather.models;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    public final double temp;

    @SerializedName("humidity")
    public final double humidity;

    @SerializedName("pressure")
    public final double pressure;

    @SerializedName("temp_min")
    public final double tempMin;

    @SerializedName("temp_max")
    public final double tempMax;

    public Main(double temp, double humidity, double pressure, double tempMin, double tempMax) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }
}
