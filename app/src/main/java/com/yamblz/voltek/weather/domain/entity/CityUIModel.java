package com.yamblz.voltek.weather.domain.entity;

import android.os.Parcel;

import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;


public class CityUIModel{

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

    protected CityUIModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CityUIModel that = (CityUIModel) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }


}
