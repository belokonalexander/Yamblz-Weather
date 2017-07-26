package com.yamblz.voltek.weather.data.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.hawk.Hawk;
import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

public class WeatherStorage implements DataProvider.Storage.Weather {

    private static final String WEATHER_KEY = "WEATHER_KEY";
    private static final String CITY_KEY = "CITY_KEY";

    @Nullable
    @Override
    public WeatherUIModel getCurrent() {
        return Hawk.get(WEATHER_KEY, null);
    }

    @Override
    public void putCurrent(@NonNull WeatherUIModel weatherUIModel) {
        Hawk.put(WEATHER_KEY, weatherUIModel);
    }

    @NonNull
    @Override
    public CityUIModel putSelectedCity(@NonNull CityUIModel city) {
        Hawk.put(CITY_KEY, city);
        return city;
    }


    // FIXME: 26.07.2017 hardcoded - seems like need to inject context here for translatable string (Alexander)
    @NonNull
    @Override
    public CityUIModel getSelectedCity() {
        return Hawk.get(CITY_KEY, new CityUIModel(524901, "Moscow"));
    }
}
