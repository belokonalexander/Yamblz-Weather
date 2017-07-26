package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.Interactor;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

public class SettingsCitySuggestionsInteractor extends Interactor<String, List<CityUIModel>> {

    private DataProvider.DataBase.CityRepository cityRepositoryUtils;


    public SettingsCitySuggestionsInteractor(Scheduler jobScheduler, Scheduler uiScheduler, DataProvider.DataBase.CityRepository cityRepositoryUtils) {
        super(jobScheduler, uiScheduler);
        this.cityRepositoryUtils = cityRepositoryUtils;

    }

    @Override
    protected Observable<Result<List<CityUIModel>>> build(Parameter<String> parameter) {
        return Observable.fromCallable(() -> cityRepositoryUtils.getCityByPrefix(parameter.getItem())).flatMap(toCity()).map(Result::new);
    }

    private Function<Collection<CityToIDModel>, ObservableSource<List<CityUIModel>>> toCity() {
        return cityToIDModels -> Observable.fromCallable(() -> {
            List<CityUIModel> list = new ArrayList<>();
            for (CityToIDModel model : cityToIDModels) {
                list.add(new CityUIModel(model.getCityId(), model.getAlias()));
            }
            return list;
        });
    }
}
