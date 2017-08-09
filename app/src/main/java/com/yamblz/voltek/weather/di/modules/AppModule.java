package com.yamblz.voltek.weather.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.DatabaseRepositoryImpl;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.storage.GsonConverter;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.data.storage.StorageRepositoryImpl;
import com.yamblz.voltek.weather.domain.interactor.FavoritesInteractor;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.presentation.ui.main.WeatherPresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;
import com.yamblz.voltek.weather.utils.rx.RxSchedulersImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 31.07.2017.
 */

@Module
public class AppModule {

    private final Context appContext;
    private final DaoSession daoSession;

    public AppModule(@NonNull Context appContext, @NonNull DaoSession daoSession) {
        this.appContext = appContext;
        this.daoSession = daoSession;

    }

    @Provides
    @Singleton
    Context provideContext() {
        return appContext;
    }

    @Provides
    @Singleton
    StorageRepository provideStorageRepository(Context context, GsonConverter gsonConverter) {
        return new StorageRepositoryImpl(context, gsonConverter);
    }

    @Provides
    @Singleton
    DaoSession provideDaoSession() {
        return daoSession;
    }


    @Provides
    @Singleton
    DatabaseRepository provideDatabaseRepository(DaoSession daoSession) {
        return new DatabaseRepositoryImpl(daoSession);
    }

    @Provides
    @Singleton
    RxSchedulers provideRxSchedulers() {
        return new RxSchedulersImpl();
    }

    @Provides
    GsonConverter gsonConverter() {
        return new GsonConverter(new GsonBuilder());
    }

    @Provides
    @Singleton
    WeatherPresenter provideFavoritesPresenter(FavoritesInteractor favoritesInteractor, RxSchedulers rxSchedulers) {
        return new WeatherPresenter(favoritesInteractor, rxSchedulers);
    }

    @Provides
    @Singleton
    FavoritesInteractor provideFavoritesInteractor(DatabaseRepository databaseRepository, StorageRepository storageRepository, RxMapper rxMapper) {
        return new FavoritesInteractor(databaseRepository, storageRepository, rxMapper);
    }

    @Provides
    @Singleton
    RxMapper provideRxMapper() {
        return new RxMapper();
    }


}
