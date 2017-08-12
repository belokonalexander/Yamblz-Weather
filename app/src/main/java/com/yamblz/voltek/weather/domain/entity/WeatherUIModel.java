package com.yamblz.voltek.weather.domain.entity;

import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;
import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastWeather;

import java.util.Date;

public class WeatherUIModel {

    private final String condition;
    private final int temperature;
    private final int humidity;
    private final int conditionId;
    private final String cityName;
    private final Date date;

    public WeatherUIModel(WeatherResponseModel model) {
        if (model.weather != null && model.weather.size() > 0)
            condition = model.weather.get(0).description;
        else condition = "";

        temperature = (int) model.main.temp;
        humidity = (int) model.main.humidity;

        if (model.weather != null && model.weather.size() > 0)
            conditionId = model.weather.get(0).id;
        else conditionId = 200;

        cityName = model.name;
        date = new Date();
    }

    public WeatherUIModel(ForecastWeather model, String city) {
        if (model.weather != null && model.weather.size() > 0) {
            condition = model.weather.get(0).description;
        } else condition = "";

        temperature = (int) model.main.temp;
        humidity = (int) model.main.humidity;

        if (model.weather != null) {
            conditionId = model.weather.get(0).id;
        } else conditionId = 200;

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

    @Override
    public String toString() {
        return "WeatherUIModel{" +
                "condition='" + condition + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", conditionId=" + conditionId +
                ", cityName='" + cityName + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeatherUIModel that = (WeatherUIModel) o;

        if (temperature != that.temperature) return false;
        if (humidity != that.humidity) return false;
        if (conditionId != that.conditionId) return false;
        if (!condition.equals(that.condition)) return false;
        if (!cityName.equals(that.cityName)) return false;
        return date.equals(that.date);

    }

    @Override
    public int hashCode() {
        int result = condition.hashCode();
        result = 31 * result + temperature;
        result = 31 * result + humidity;
        result = 31 * result + conditionId;
        result = 31 * result + cityName.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }
}
