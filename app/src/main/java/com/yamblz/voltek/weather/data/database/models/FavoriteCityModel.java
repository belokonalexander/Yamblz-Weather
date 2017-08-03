package com.yamblz.voltek.weather.data.database.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;


@Entity(indexes = {@Index(value = "cityId", unique = true)})
public class FavoriteCityModel {

    @Id
    private Long Id;

    @NotNull
    private String alias;

    @NotNull
    private Integer cityId;


    public FavoriteCityModel(@NotNull String alias, @NotNull Integer cityId) {
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



    @Generated(hash = 807387374)
    public FavoriteCityModel() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoriteCityModel that = (FavoriteCityModel) o;

        if (!alias.equals(that.alias)) return false;
        return cityId.equals(that.cityId);

    }

    @Override
    public int hashCode() {
        int result = alias.hashCode();
        result = 31 * result + cityId.hashCode();
        return result;
    }
}
