package com.yamblz.voltek.weather;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yamblz.voltek.weather.data.api.ApiConst;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPIDelegate;
import com.yamblz.voltek.weather.data.storage.WeatherStorage;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsCityInteractor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public final class Injector {

    private Injector() {
    }

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    private static Retrofit retrofit;

    public static void init(Context context) {
        Injector.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConst.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(provideHttpClient())
                .build();
    }

    private static OkHttpClient provideHttpClient() {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            return httpClient.build();
        } else {
            return new OkHttpClient();
        }
    }

    // CurrentWeatherInteractor
    private static CurrentWeatherInteractor currentWeatherInteractor;

    public static CurrentWeatherInteractor currentWeatherInteractor() {
        if (currentWeatherInteractor == null) {
            currentWeatherInteractor = new CurrentWeatherInteractor(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread(),
                    new WeatherAPIDelegate(context, retrofit.create(WeatherAPI.class)),
                    new WeatherStorage()
            );
        }

        return currentWeatherInteractor;
    }

    public static SettingsCityInteractor settingsInteractor;

    public static SettingsCityInteractor settingsInteractor() {
        if (settingsInteractor == null) {
            settingsInteractor = new SettingsCityInteractor(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread()
            );
        }

        return settingsInteractor;
    }


}
