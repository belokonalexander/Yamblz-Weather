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
    String CITY_KEY = "CITY_KEY";
    String UNITS_KEY = "UNITS_KEY";
    String START_COUNT_KEY = "START_COUNT_KEY";
    String UPDATE_INTERVAL_KEY = "UPDATE_INTERVAL_KEY";
    String ENABLE_UPDATE_KEY = "ENABLE_UPDATE_KEY";


    Completable putSelectedCity(@NonNull CityUIModel city);

    Single<String> getUnitsSettings();

    Single<CityUIModel> getSelectedCity();

    Single<Boolean> getUpdateEnabled();

    Single<Integer> getUpdateInterval();

    Single<Boolean> getApplicationStartFirst();

    Completable incrementApplicationStartCount();

    Completable fillByDefaultData();
}
