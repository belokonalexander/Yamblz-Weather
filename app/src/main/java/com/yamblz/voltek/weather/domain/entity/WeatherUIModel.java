package com.yamblz.voltek.weather.domain.entity;

import com.yamblz.voltek.weather.data.api.weather.WeatherResponseModel;

public class WeatherUIModel {

    private String condition;
    private int temperature;
    private int humidity;
    private int conditionId;

    public WeatherUIModel(WeatherResponseModel model) {
        condition = model.weather.get(0).description;
        temperature = model.main.temp.intValue();
        humidity = model.main.humidity;
        conditionId = model.weather.get(0).id;
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
}
