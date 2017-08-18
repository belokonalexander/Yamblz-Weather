package com.yamblz.voltek.weather.data.storage;

import com.google.gson.GsonBuilder;

/**
 * Created on 01.08.2017.
 */

public class GsonConverter {

    private final GsonBuilder gsonBuilder;

    public GsonConverter(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public String toJsonString(Object object) {
        return gsonBuilder.create().toJson(object);
    }

    public <T> T toJsonObject(Class<T> type, String json) {
        return gsonBuilder.create().fromJson(json, type);
    }
}
