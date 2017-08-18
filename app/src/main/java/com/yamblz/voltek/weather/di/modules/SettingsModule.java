package com.yamblz.voltek.weather.di.modules;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.platform.jobs.JobWrapper;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.di.scopes.SettingsScope;
import com.yamblz.voltek.weather.domain.interactor.CitySettingsInteractor;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsPresenter;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsSelectCityPresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 31.07.2017.
 */

@Module
public class SettingsModule {


    @Provides
    @SettingsScope
    CitySettingsInteractor provideCitySettingsInteractor(WeatherAPI weatherApi, DatabaseRepository databaseRepository, RxMapper rxMapper) {
        return new CitySettingsInteractor(weatherApi, databaseRepository, rxMapper);
    }

    @Provides
    @SettingsScope
    SettingsSelectCityPresenter provideSettingsSelectCityPresenter(CitySettingsInteractor settingsInteractor, RxSchedulers rxSchedulers) {
        return new SettingsSelectCityPresenter(settingsInteractor, rxSchedulers);
    }

    @Provides
    @SettingsScope
    SettingsInteractor provideSettingsInteractor(JobWrapper jobWrapper, StorageRepository storageRepository) {
        return new SettingsInteractor(jobWrapper, storageRepository);
    }

    @Provides
    @SettingsScope
    SettingsPresenter provideSettingsPresenter(SettingsInteractor settingsInteractor, RxSchedulers rxSchedulers) {
        return new SettingsPresenter(settingsInteractor, rxSchedulers);
    }

}
