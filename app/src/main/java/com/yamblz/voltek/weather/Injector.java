package com.yamblz.voltek.weather;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yamblz.voltek.weather.data.api.ApiConst;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPIDelegate;
import com.yamblz.voltek.weather.data.database.AppDatabaseHelper;
import com.yamblz.voltek.weather.data.database.CityRepositoryStorage;
import com.yamblz.voltek.weather.data.database.models.DaoMaster;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.storage.WeatherStorage;
import com.yamblz.voltek.weather.domain.interactor.CurrentSettingsInteractor;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsCitySuggestionsInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsSetCityInteractor;

import org.greenrobot.greendao.database.Database;

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

    private static DaoSession session;

    public static void init(Context context) {
        Injector.context = context;

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConst.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(provideHttpClient())
                .build();

        DaoMaster.DevOpenHelper helper = new AppDatabaseHelper(context, "weather.db", null);
        Database db = helper.getWritableDb();
        session = new DaoMaster(db).newSession();

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

    private static SettingsCitySuggestionsInteractor settingsCitySuggestionsInteractor;

    public static SettingsCitySuggestionsInteractor settingsCitySuggestionsInteractor() {
        if (settingsCitySuggestionsInteractor == null) {
            settingsCitySuggestionsInteractor = new SettingsCitySuggestionsInteractor(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread(),
                    new CityRepositoryStorage(session.getCityToIDModelDao())
            );
        }

        return settingsCitySuggestionsInteractor;
    }

    private static SettingsSetCityInteractor settingsSetCityInteractor;

    public static SettingsSetCityInteractor settingsSetCityInteractor() {
        if (settingsSetCityInteractor == null) {
            settingsSetCityInteractor = new SettingsSetCityInteractor(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread(),
                    new WeatherAPIDelegate(context, retrofit.create(WeatherAPI.class)),
                    new WeatherStorage(),
                    new CityRepositoryStorage(session.getCityToIDModelDao())
            );
        }

        return settingsSetCityInteractor;
    }

    private static CurrentSettingsInteractor currentSettingsInteractor;

    public static CurrentSettingsInteractor currentSettingsInteractor() {
        if (currentSettingsInteractor == null) {
            currentSettingsInteractor = new CurrentSettingsInteractor(
                    Schedulers.io(),
                    AndroidSchedulers.mainThread(),
                    new WeatherStorage()
            );
        }

        return currentSettingsInteractor;
    }
}
