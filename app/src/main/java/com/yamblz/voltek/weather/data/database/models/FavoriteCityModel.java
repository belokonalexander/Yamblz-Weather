package com.yamblz.voltek.weather.data.database.models;

import android.support.annotation.Nullable;

import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;
import com.yamblz.voltek.weather.data.database.converters.ForecastConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;


@Entity()
public class FavoriteCityModel {

    @Id
    private Long Id;

    @NotNull
    private String alias;

    @Unique
    @NotNull
    private Integer cityId;

    @Nullable
    @Convert(converter = ForecastConverter.class, columnType = String.class)
    private ForecastResponseModel forecast;

    public FavoriteCityModel() {
    }

    public FavoriteCityModel(@NotNull String alias, @NotNull Integer cityId) {
        this.alias = alias;
        this.cityId = cityId;
    }

    public FavoriteCityModel(@NotNull String alias, @NotNull Integer cityId, @Nullable ForecastResponseModel forecast) {
        this.alias = alias;
        this.cityId = cityId;
        this.forecast = forecast;
    }

    @Keep
    public FavoriteCityModel(Long Id, @NotNull String alias, @NotNull Integer cityId, @Nullable ForecastResponseModel forecast) {
        this.Id = Id;
        this.alias = alias;
        this.cityId = cityId;
        this.forecast = forecast;
    }


    public Long getId() {
        return this.Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getCityId() {
        return this.cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    @Nullable
    public ForecastResponseModel getForecast() {
        return this.forecast;
    }

    public void setForecast(@Nullable ForecastResponseModel forecast) {
        this.forecast = forecast;
    }


}
