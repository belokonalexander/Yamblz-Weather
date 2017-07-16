package com.yamblz.voltek.weather.data.api.weather;

import com.squareup.moshi.Json;

public class Coord {

    @Json(name = "lon")
    public Double lon;

    @Json(name = "lat")
    public Double lat;
}
