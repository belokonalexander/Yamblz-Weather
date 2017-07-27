package com.yamblz.voltek.weather.data.platform;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.yamblz.voltek.weather.Injector;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;

import timber.log.Timber;

public class UpdateCurrentWeatherService extends IntentService {

    private static final String TAG = "UpdateCurrentWeatherService";

    public UpdateCurrentWeatherService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Injector.init(getApplicationContext());

        CurrentWeatherInteractor interactor = Injector.currentWeatherInteractor();
        Parameter<Void> param = new Parameter<>();
        param.setFlag(CurrentWeatherInteractor.REFRESH);

        interactor.execute(
                param,
                result -> interactor.unsubscribe(),
                Throwable::printStackTrace,
                () -> Timber.d("Current weather successfully update in background")
        );
    }
}
