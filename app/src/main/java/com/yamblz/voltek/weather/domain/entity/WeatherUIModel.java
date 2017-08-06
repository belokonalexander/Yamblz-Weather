package com.yamblz.voltek.weather.domain.entity;

import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;
import com.yamblz.voltek.weather.data.api.weather.response.forecast.ForecastWeather;

import java.util.Date;

public class WeatherUIModel {

    private String condition;
    private int temperature;
    private int humidity;
    private int conditionId;
    private String cityName;
    private Date date;

    public WeatherUIModel(WeatherResponseModel model) {
        condition = model.weather.get(0).description;
        temperature = (int) model.main.temp;
        humidity = (int) model.main.humidity;
        conditionId = model.weather.get(0).id;
        cityName = model.name;
        date = new Date();
    }

    public WeatherUIModel(ForecastWeather model, String city) {
        condition = model.weather.get(0).description;
        temperature = (int) model.main.temp;
        humidity = (int) model.main.humidity;
        conditionId = model.weather.get(0).id;
        date = model.date;
        cityName = city;

    }

    public String getCondition() {
        return condition;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getConditionId() {
        return conditionId;
    }

    public String getCityName() {
        return cityName;
    }

    public Date getDate() {
        return date;
    }
}
