package com.yamblz.voltek.weather.domain.entity;

import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;


public class CityUIModel {

    public final int id;

    public final String name;

    public CityUIModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityUIModel(String name) {
        this.name = name;
        id = -1;
    }

    public CityUIModel(WeatherResponseModel body) {
        this(body.id, body.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
