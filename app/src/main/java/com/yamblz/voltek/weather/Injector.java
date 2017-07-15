package com.yamblz.voltek.weather;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPIDelegate;
import com.yamblz.voltek.weather.data.storage.WeatherStorage;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public final class Injector {

    private Injector() {}

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static Retrofit retrofit;

    static void init(Context context) {
        Injector.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(new OkHttpClient())
                .build();
    }

    public static CurrentWeatherInteractor currentWeatherInteractor() {
        return new CurrentWeatherInteractor(
                Schedulers.io(),
                AndroidSchedulers.mainThread(),
                new WeatherAPIDelegate(context, retrofit.create(WeatherAPI.class)),
                new WeatherStorage()
        );
    }
}
