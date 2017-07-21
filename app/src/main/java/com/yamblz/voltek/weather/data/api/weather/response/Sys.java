package com.yamblz.voltek.weather.data.api.weather.response;

import com.squareup.moshi.Json;

public class Sys {

    @Json(name = "country")
    public String country;

    @Json(name = "sunrise")
    public int sunrise;

    @Json(name = "sunset")
    public int sunset;
}
