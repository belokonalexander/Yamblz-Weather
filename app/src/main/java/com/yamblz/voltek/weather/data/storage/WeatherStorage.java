package com.yamblz.voltek.weather.data.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.hawk.Hawk;
import com.yamblz.voltek.weather.data.Provider;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

public class WeatherStorage implements Provider.Storage.Weather {

    private static final String WEATHER_KEY = "WEATHER_KEY";

    @Nullable
    @Override
    public WeatherUIModel getCurrent() {
        return Hawk.get(WEATHER_KEY, null);
    }

    @Override
    public void putCurrent(@NonNull WeatherUIModel weatherUIModel) {
        Hawk.put(WEATHER_KEY, weatherUIModel);
    }
}
