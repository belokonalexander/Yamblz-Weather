package com.yamblz.voltek.weather.data.storage;

import android.support.annotation.NonNull;

import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created on 31.07.2017.
 */

public interface StorageRepository {

    Single<WeatherUIModel> getCurrent();

    Completable putCurrent(@NonNull WeatherUIModel weatherUIModel);

    Completable putSelectedCity(@NonNull CityUIModel city);

    Single<CityUIModel> getSelectedCity();

    Single<Boolean> getUpdateEnabled();

    Single<Integer> getUpdateInterval();

}
