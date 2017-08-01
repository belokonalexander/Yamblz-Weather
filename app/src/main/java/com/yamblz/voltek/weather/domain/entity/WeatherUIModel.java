package com.yamblz.voltek.weather.domain.entity;

import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;

public class WeatherUIModel {

    private String condition;
    private int temperature;
    private int humidity;
    private int conditionId;
    private String cityName;

    public WeatherUIModel(WeatherResponseModel model) {
        condition = model.weather.get(0).description;
        temperature = (int) model.main.temp;
        humidity = (int) model.main.humidity;
        conditionId = model.weather.get(0).id;
        cityName = model.name;
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
}
