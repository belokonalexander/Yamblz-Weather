package com.yamblz.voltek.weather.di.modules;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.interactor.FavoritesInteractor;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.presentation.ui.main.WeatherPresenter;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17.08.2017.
 */
@Module
public class WeatherModule {

    @Provides
    @Singleton
    WeatherPresenter provideFavoritesPresenter(FavoritesInteractor favoritesInteractor, RxSchedulers rxSchedulers) {
        return new WeatherPresenter(favoritesInteractor, rxSchedulers, new SetWithSelection<>());
    }

    @Provides
    @Singleton
    FavoritesInteractor provideFavoritesInteractor(DatabaseRepository databaseRepository, StorageRepository storageRepository, RxMapper rxMapper) {
        return new FavoritesInteractor(databaseRepository, storageRepository, rxMapper);
    }

}
