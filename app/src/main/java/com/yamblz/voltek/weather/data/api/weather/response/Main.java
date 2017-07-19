package com.yamblz.voltek.weather.data.api.weather.response;

import com.squareup.moshi.Json;

public class Main {

    @Json(name = "temp")
    public double temp;

    @Json(name = "humidity")
    public int humidity;

    @Json(name = "pressure")
    public int pressure;

    @Json(name = "temp_min")
    public int tempMin;

    @Json(name = "temp_max")
    public int tempMax;
}
