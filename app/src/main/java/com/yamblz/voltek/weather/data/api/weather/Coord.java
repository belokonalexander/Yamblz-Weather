package com.yamblz.voltek.weather.data.api.weather;

import com.squareup.moshi.Json;

public class Coord {

    @Json(name = "lon")
    public Integer lon;

    @Json(name = "lat")
    public Integer lat;
}
