package com.yamblz.voltek.weather;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yamblz.voltek.weather.data.Provider;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPIDelegate;

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

    public static Provider.API.Weather weatherApi() {
        return new WeatherAPIDelegate(context, retrofit.create(WeatherAPI.class));
    }
}
