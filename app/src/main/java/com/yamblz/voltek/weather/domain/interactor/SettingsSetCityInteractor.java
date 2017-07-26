package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.Interactor;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.Result;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class SettingsSetCityInteractor extends Interactor<CityUIModel, CityUIModel> {

    private DataProvider.API.Weather api;
    private DataProvider.Storage.Weather storage;
    private DataProvider.DataBase.CityRepository cityRepositoryUtils;

    public SettingsSetCityInteractor(Scheduler jobScheduler, Scheduler uiScheduler,
                                     DataProvider.API.Weather api,
                                     DataProvider.Storage.Weather storage,
                                     DataProvider.DataBase.CityRepository cityRepositoryUtils
    ) {
        super(jobScheduler, uiScheduler);
        this.api = api;
        this.storage = storage;
        this.cityRepositoryUtils = cityRepositoryUtils;
    }


    /**
     * if city hasn't id:
     * 1.a) try to get the id from local database
     * 1.b) try to get the id from api
     *  1b.1) save api's id with name to local database
     * 2) if chain is correct -> update shared pref's value with new city value
     *
     * @param parameter the city
     * @return true result if task was successfully completed
     */
    @Override
    protected Observable<Result<CityUIModel>> build(Parameter<CityUIModel> parameter) {
        CityUIModel city = parameter.getItem();

        if (city == null)
            throw new IllegalArgumentException();

        if (city.id > 0) {
            return getCorrectObservable(city);
        } else {
            return Observable.fromCallable(() -> cityRepositoryUtils.getCityByName(city.name)).onErrorResumeNext(throwable -> {
                return Observable.fromCallable(() -> api.getCity(city.name))
                        .map(city12 -> cityRepositoryUtils.saveCity(new CityToIDModel(city12.name, city12.id)));
            }).flatMap(new Function<CityToIDModel, ObservableSource<Result<CityUIModel>>>() {
                @Override
                public ObservableSource<Result<CityUIModel>> apply(@NonNull CityToIDModel cityToIDModel) throws Exception {
                    return getCorrectObservable(new CityUIModel(cityToIDModel.getCityId(), cityToIDModel.getAlias()));
                }
            });
        }
    }

    private Observable<Result<CityUIModel>> getCorrectObservable(CityUIModel city) {
        return Observable.fromCallable(() -> storage.putSelectedCity(city)).map(this::wrapResult);
    }

    private Result<CityUIModel> wrapResult(CityUIModel res) {
        return new Result<>(res);
    }
}
