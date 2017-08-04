package com.yamblz.voltek.weather.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import java.util.Locale;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Single;

public class StorageRepositoryImpl implements StorageRepository {

    private SharedPreferences settings;
    private GsonConverter converter;

    private String defaultUnits = "Metric";
    private CityUIModel defaultCity;
    private int defaultUpdateInterval = 15;
    private boolean defaultUpdateEnabled = false;

    public StorageRepositoryImpl(Context context, GsonConverter converter) {
        this.settings = context.getSharedPreferences(CATEGORY_SETTINGS, 0);
        this.converter = converter;

        String countryCode = Locale.getDefault().getCountry();
        if ("US".equals(countryCode) || "LR".equals(countryCode) || "MM".equals(countryCode))
            defaultUnits = "Imperial";

        defaultCity = new CityUIModel(context.getResources().getInteger(R.integer.default_city_id),
                context.getResources().getString(R.string.default_city_name));

    }

    @Override
    public Completable putSelectedCity(@NonNull CityUIModel city) {
        return Completable.fromAction(() -> settings.edit().putString(CITY_KEY, converter.toJsonString(city)).apply());
    }

    @Override
    public Single<String> getUnitsSettings() {
        return Single.fromCallable(() -> settings.getString(UNITS_KEY, defaultUnits));
    }

    @Override
    public Single<CityUIModel> getSelectedCity() {
        return Single.fromCallable(getStoreOrDefault(settings, CITY_KEY, defaultCity, CityUIModel.class));
    }

    @Override
    public Single<Boolean> getUpdateEnabled() {
        return Single.fromCallable(() -> settings.getBoolean(ENABLE_UPDATE_KEY, defaultUpdateEnabled));
    }

    @Override
    public Single<Integer> getUpdateInterval() {
        return Single.fromCallable(() -> settings.getInt(UPDATE_INTERVAL_KEY, defaultUpdateInterval));
    }

    @Override
    public Single<Boolean> getApplicationStartFirst() {
        return Single.fromCallable(() -> settings.getBoolean(START_COUNT_KEY, true));
    }

    @Override
    public Completable incrementApplicationStartCount() {
        return Completable.fromAction(() -> settings.edit().putBoolean(START_COUNT_KEY, false).apply());
    }

    @Override
    public Completable fillByDefaultData() {
        return Completable.fromAction(() -> {

            putSelectedCity(defaultCity).subscribe();
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(UPDATE_INTERVAL_KEY, String.valueOf(defaultUpdateInterval));
            editor.putString(UNITS_KEY, defaultUnits);
            editor.putBoolean(ENABLE_UPDATE_KEY, defaultUpdateEnabled);
            editor.putString(CITY_KEY, converter.toJsonString(defaultCity));
            editor.apply();
            incrementApplicationStartCount().subscribe();
        });
    }

    private <T> Callable<T> getStoreOrDefault(SharedPreferences preferences, String key, T defaultValue, Class<T> helpType) {
        String json = preferences.getString(key, "");
        if (json.length() == 0) {
            return () -> defaultValue;
        } else {
            return () -> converter.toJsonObject(helpType, json);
        }
    }
}
