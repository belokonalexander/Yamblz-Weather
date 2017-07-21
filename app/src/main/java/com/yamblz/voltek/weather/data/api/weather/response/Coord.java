package com.yamblz.voltek.weather.data.api.weather.response;

import com.squareup.moshi.Json;

public class Coord {

    @Json(name = "lon")
    public double lon;

    @Json(name = "lat")
    public double lat;
}
