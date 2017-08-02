package com.yamblz.voltek.weather.di.modules;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.di.scopes.ForecastScope;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastPresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 31.07.2017.
 */

@Module
public class ForecastModule {

    @Provides
    @ForecastScope
    ForecastPresenter provideForecastPresenter(ForecastInteractor forecastInteractor, RxSchedulers rxSchedulers) {
        return new ForecastPresenter(forecastInteractor, rxSchedulers);
    }

    @Provides
    @ForecastScope
    ForecastInteractor provideForecastInteractor(WeatherAPI weatherApi, StorageRepository storageRepository) {
        return new ForecastInteractor(weatherApi,storageRepository);
    }

}