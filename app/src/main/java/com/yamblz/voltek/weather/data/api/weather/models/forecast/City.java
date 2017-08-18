package com.yamblz.voltek.weather.data.api.weather.models.forecast;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 06.08.2017.
 */

public class City {

    @SerializedName("id")
    public final int id;

    @SerializedName("name")
    public String name;

    @SerializedName("country")
    public final String country;

    public City(int id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }
}
