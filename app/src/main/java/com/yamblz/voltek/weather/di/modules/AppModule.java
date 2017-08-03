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
import com.yamblz.voltek.weather.presentation.ui.favorites.FavoritesPresenter;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;
import com.yamblz.voltek.weather.utils.rx.RxSchedulersWork;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 31.07.2017.
 */

@Module
public class AppModule {

    private Context appContext;
    private DaoSession daoSession;

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
        return new RxSchedulersWork();
    }

    @Provides
    GsonConverter gsonConverter() {
        return new GsonConverter(new GsonBuilder());
    }

    @Provides
    @Singleton
    FavoritesPresenter provideFavoritesPresenter(FavoritesInteractor favoritesInteractor) {
        return new FavoritesPresenter(favoritesInteractor);
    }

    @Provides
    @Singleton
    FavoritesInteractor provideFavoritesInteractor(DatabaseRepository databaseRepository) {
        return new FavoritesInteractor(databaseRepository);
    }
}
