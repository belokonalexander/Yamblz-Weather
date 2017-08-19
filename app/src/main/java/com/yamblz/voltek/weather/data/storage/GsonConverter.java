package com.yamblz.voltek.weather.data.storage;

import com.google.gson.Gson;

/**
 * Created on 01.08.2017.
 */

public class GsonConverter {

    private final Gson gson;

    public GsonConverter(Gson gson) {
        this.gson = gson;
    }

    public String toJsonString(Object object) {
        return gson.toJson(object);
    }

    public <T> T toJsonObject(Class<T> type, String json) {
        return gson.fromJson(json, type);
    }
}
