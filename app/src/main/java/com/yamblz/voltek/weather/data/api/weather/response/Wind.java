package com.yamblz.voltek.weather.data.api.weather.response;

import com.squareup.moshi.Json;

public class Wind {

    @Json(name = "speed")
    public double speed;

    @Json(name = "deg")
    public double deg;
}
