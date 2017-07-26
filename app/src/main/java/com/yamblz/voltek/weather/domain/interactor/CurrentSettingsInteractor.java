package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.Interactor;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;

import io.reactivex.Observable;
import io.reactivex.Scheduler;


public class CurrentSettingsInteractor extends Interactor<Void, CityUIModel> {

    private DataProvider.Storage.Weather storage;

    public CurrentSettingsInteractor(Scheduler jobScheduler, Scheduler uiScheduler,
                                     DataProvider.Storage.Weather storage
    ) {
        super(jobScheduler, uiScheduler);

        this.storage = storage;

    }

    @Override
    protected Observable<Result<CityUIModel>> build(Parameter<Void> parameter) {
        return Observable.fromCallable(() -> {
            CityUIModel city = storage.getSelectedCity();
            return new Result<>(city);
        });

    }


}
