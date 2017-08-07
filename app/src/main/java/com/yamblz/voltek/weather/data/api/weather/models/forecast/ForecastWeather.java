package com.yamblz.voltek.weather.data.api.weather.models.forecast;

import com.google.gson.annotations.SerializedName;
import com.yamblz.voltek.weather.data.api.weather.models.Main;
import com.yamblz.voltek.weather.data.api.weather.models.Weather;

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
