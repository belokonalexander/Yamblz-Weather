package com.yamblz.voltek.weather.di.modules;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.di.scopes.SettingsScope;
import com.yamblz.voltek.weather.domain.interactor.SettingsInteractor;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsPresenter;
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
    SettingsInteractor provideSettingsInteractor(WeatherAPI weatherApi, StorageRepository storageRepository, DatabaseRepository databaseRepository) {
        return new SettingsInteractor(weatherApi, storageRepository, databaseRepository);
    }

    @Provides
    @SettingsScope
    SettingsPresenter provideSettingsPresenter(SettingsInteractor settingsInteractor, RxSchedulers rxSchedulers) {
        return new SettingsPresenter(settingsInteractor, rxSchedulers);
    }


}
