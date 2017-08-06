package com.yamblz.voltek.weather.data.api.weather.response.forecast;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 06.08.2017.
 */

public class City {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("country")
    public String country;

}
