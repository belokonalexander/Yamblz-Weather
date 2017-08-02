package com.yamblz.voltek.weather.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;

public class StorageRepositoryImpl implements StorageRepository {

    private SharedPreferences settings;
    private SharedPreferences store;
    private GsonConverter converter;

    public StorageRepositoryImpl(Context context, GsonConverter converter) {
        this.settings = context.getSharedPreferences(CATEGORY_SETTINGS, 0);
        this.store = context.getSharedPreferences(CATEGORY_STORE, 0);
        this.converter = converter;
    }

  /*  @Override
    public Completable putCurrent(@NonNull WeatherUIModel weatherUIModel) {
        return Completable.fromAction(() -> Hawk.put(WEATHER_KEY, weatherUIModel));
    }*/

    @Override
    public Completable putSelectedCity(@NonNull CityUIModel city) {
        return Completable.fromAction(() -> settings.edit().putString(CITY_KEY, converter.toJsonString(city)).apply());
    }

    @Override
    public Single<CityUIModel> getSelectedCity() {
        return Single.fromCallable(getStoreOrDefault(settings, CITY_KEY, new CityUIModel(524901, "Moscow"), CityUIModel.class));
    }

    @Override
    public Single<Boolean> getUpdateEnabled() {
        return Single.fromCallable(() -> settings.getBoolean(ENABLE_UPDATE_KEY, false));
    }

    @Override
    public Single<Integer> getUpdateInterval() {
        return Single.fromCallable(() -> settings.getInt(UPDATE_INTERVAL_KEY, 15));
    }

   /* @Override
    public Single<WeatherUIModel> getCurrent() {
        return Single.fromCallable(() -> Hawk.get(WEATHER_KEY));

    }*/

    private <T> Callable<T> getStoreOrDefault(SharedPreferences preferences, String key, T defaultValue, Class<T> helpType) {
        String json = preferences.getString(key, "");
        if (json.length() == 0) {
            return () -> defaultValue;
        } else {
            return () -> converter.toJsonObject(helpType, json);
        }
    }
}
