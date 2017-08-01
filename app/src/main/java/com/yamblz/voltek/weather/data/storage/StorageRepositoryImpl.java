package com.yamblz.voltek.weather.data.storage;

import android.support.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

import io.reactivex.Completable;
import io.reactivex.Single;

public class StorageRepositoryImpl implements StorageRepository {

    private static final String WEATHER_KEY = "WEATHER_KEY";
    private static final String CITY_KEY = "CITY_KEY";


    @Override
    public Completable putCurrent(@NonNull WeatherUIModel weatherUIModel) {
        return Completable.fromAction(() -> Hawk.put(WEATHER_KEY, weatherUIModel));
    }


    @Override
    public Completable putSelectedCity(@NonNull CityUIModel city) {
        return Completable.fromAction(() -> Hawk.put(CITY_KEY, city));
    }


    @Override
    public Single<CityUIModel> getSelectedCity() {
        return Single.fromCallable(() -> Hawk.get(CITY_KEY, new CityUIModel(524901, "Moscow")));
    }

    @Override
    public Single<Boolean> getUpdateEnabled() {
        return null;
    }

    @Override
    public Single<Integer> getUpdateInterval() {
        return null;
    }

    @Override
    public Single<WeatherUIModel> getCurrent() {
        return Single.fromCallable(() -> Hawk.get(WEATHER_KEY));

    }
}
