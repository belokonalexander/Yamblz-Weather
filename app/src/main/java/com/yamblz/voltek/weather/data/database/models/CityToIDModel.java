package com.yamblz.voltek.weather.data.database.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * table with names of cities and associated city-id
 */
@Entity(indexes = {@Index(value = "cityId,alias", unique = true)})
public class CityToIDModel {

    @Id
    private Long Id;

    @NotNull
    private String alias;

    @NotNull
    private Integer cityId;


    public CityToIDModel(@NotNull String alias, @NotNull Integer cityId) {
        this.alias = alias;
        this.cityId = cityId;
    }

    @Generated(hash = 122666723)
    public CityToIDModel(Long Id, @NotNull String alias, @NotNull Integer cityId) {
        this.Id = Id;
        this.alias = alias;
        this.cityId = cityId;
    }


    @Generated(hash = 447841134)
    public CityToIDModel() {
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

        CityToIDModel that = (CityToIDModel) o;

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
