package com.yamblz.voltek.weather.data.database.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
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

    public FavoriteCityModel() {
    }

    public FavoriteCityModel(String alias, Integer cityId) {
        this.alias = alias;
        this.cityId = cityId;
    }

    @Generated(hash = 1030219606)
    public FavoriteCityModel(Long Id, @NotNull String alias,
            @NotNull Integer cityId) {
        this.Id = Id;
        this.alias = alias;
        this.cityId = cityId;
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





}
