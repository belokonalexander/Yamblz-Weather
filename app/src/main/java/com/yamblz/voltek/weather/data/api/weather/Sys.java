package com.yamblz.voltek.weather.data.api.weather;

import com.squareup.moshi.Json;

public class Sys {

    @Json(name = "country")
    public String country;

    @Json(name = "sunrise")
    public Integer sunrise;

    @Json(name = "sunset")
    public Integer sunset;
}
