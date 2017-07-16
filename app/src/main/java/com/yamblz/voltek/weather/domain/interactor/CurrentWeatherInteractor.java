package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.Provider;
import com.yamblz.voltek.weather.domain.Interactor;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class CurrentWeatherInteractor extends Interactor<Void, WeatherUIModel> {

    public static final String REFRESH = "REFRESH";

    private Provider.API.Weather api;
    private Provider.Storage.Weather storage;

    public CurrentWeatherInteractor(
            Scheduler jobScheduler, Scheduler uiScheduler,
            Provider.API.Weather api, Provider.Storage.Weather storage
    ) {
        super(jobScheduler, uiScheduler);
        this.api = api;
        this.storage = storage;
    }

    @Override
    protected Observable<Result<WeatherUIModel>> build(Parameter<Void> parameter) {
        return Observable.create(emitter -> {
            WeatherUIModel item = storage.getCurrent();
            if (item == null || Objects.equals(parameter.getFlag(), REFRESH)) {
                try {
                    item = api.getCurrent();
                    storage.putCurrent(item);
                    emitter.onNext(new Result<>(item));
                } catch (Exception e) {
                    emitter.onError(e);
                }
            } else {
                emitter.onNext(new Result<>(item));
            }

            emitter.onComplete();
        });
    }
}
