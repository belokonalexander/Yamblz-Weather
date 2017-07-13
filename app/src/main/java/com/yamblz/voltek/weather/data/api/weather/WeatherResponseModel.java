package com.yamblz.voltek.weather.data.api.weather;

import java.util.List;
import com.squareup.moshi.Json;

public class WeatherResponseModel {

    @Json(name = "coord")
    public Coord coord;

    @Json(name = "sys")
    public Sys sys;

    @Json(name = "weather")
    public List<Weather> weather = null;

    @Json(name = "main")
    public Main main;

    @Json(name = "wind")
    public Wind wind;

    @Json(name = "rain")
    public Rain rain;

    @Json(name = "clouds")
    public Clouds clouds;

    @Json(name = "dt")
    public Integer dt;

    @Json(name = "id")
    public Integer id;

    @Json(name = "name")
    public String name;

    @Json(name = "cod")
    public Integer cod;
}
