package com.yamblz.voltek.weather.data.api.weather.response;

import com.squareup.moshi.Json;

public class Main {

    @Json(name = "temp")
    public double temp;

    @Json(name = "humidity")
    public double humidity;

    @Json(name = "pressure")
    public double pressure;

    @Json(name = "temp_min")
    public double tempMin;

    @Json(name = "temp_max")
    public double tempMax;
}
