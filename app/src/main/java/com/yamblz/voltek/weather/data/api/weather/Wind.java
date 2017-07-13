package com.yamblz.voltek.weather.data.api.weather;

import com.squareup.moshi.Json;

public class Wind {

    @Json(name = "speed")
    public Double speed;

    @Json(name = "deg")
    public Double deg;
}
