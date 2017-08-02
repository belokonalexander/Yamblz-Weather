package com.yamblz.voltek.weather.data.storage;

import android.support.annotation.NonNull;

import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created on 31.07.2017.
 */

public interface StorageRepository {

    String CATEGORY_SETTINGS = "settings";
    String CATEGORY_STORE = "store";

    String WEATHER_KEY = "WEATHER_KEY";
    String CITY_KEY = "CITY_KEY";
    String UPDATE_INTERVAL_KEY = "UPDATE_INTERVAL_KEY";
    String ENABLE_UPDATE_KEY = "ENABLE_UPDATE_KEY";

    //Single<WeatherUIModel> getCurrent();

    //Completable putCurrent(@NonNull WeatherUIModel weatherUIModel);

    Completable putSelectedCity(@NonNull CityUIModel city);

    Single<CityUIModel> getSelectedCity();

    Single<Boolean> getUpdateEnabled();

    Single<Integer> getUpdateInterval();

}
