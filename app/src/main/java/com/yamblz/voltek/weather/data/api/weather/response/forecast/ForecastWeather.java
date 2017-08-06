package com.yamblz.voltek.weather.data.api.weather.response.forecast;

import com.google.gson.annotations.SerializedName;
import com.yamblz.voltek.weather.data.api.weather.response.Main;
import com.yamblz.voltek.weather.data.api.weather.response.Weather;

import java.util.Date;
import java.util.List;

/**
 * Created on 06.08.2017.
 */

public class ForecastWeather {

    @SerializedName("main")
    public Main main;

    @SerializedName("weather")
    public List<Weather> weather = null;

    @SerializedName("dt_txt")
    public Date date;

}
