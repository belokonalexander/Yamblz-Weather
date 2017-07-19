package com.yamblz.voltek.weather.data.api.weather.response;

import com.squareup.moshi.Json;

public class Weather {

    @Json(name = "id")
    public int id;

    @Json(name = "main")
    public String main;

    @Json(name = "description")
    public String description;

    @Json(name = "icon")
    public String icon;
}
