package com.yamblz.voltek.weather.data.api.weather;

import com.squareup.moshi.Json;

public class Main {

    @Json(name = "temp")
    public Double temp;

    @Json(name = "humidity")
    public Integer humidity;

    @Json(name = "pressure")
    public Integer pressure;

    @Json(name = "temp_min")
    public Double tempMin;

    @Json(name = "temp_max")
    public Double tempMax;
}
