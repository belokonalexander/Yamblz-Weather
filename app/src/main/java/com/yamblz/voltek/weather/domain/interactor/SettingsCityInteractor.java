package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.domain.Interactor;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class SettingsCityInteractor extends Interactor<String, List<String>> {

    public SettingsCityInteractor(Scheduler jobScheduler, Scheduler uiScheduler) {
        super(jobScheduler, uiScheduler);
    }

    @Override
    protected Observable<Result<List<String>>> build(Parameter<String> parameter) {
        return Observable.create(emitter -> {
            List<String> list = new ArrayList<String>();
            list.add(parameter.getItem());
            Result<List<String>> result = new Result<List<String>>(list);
            emitter.onNext(result);
            emitter.onComplete();
        });
    }
}
