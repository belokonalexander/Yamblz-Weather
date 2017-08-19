package com.yamblz.voltek.weather.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.storage.GsonConverter;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
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
    DaoSession provideDaoSession() {
        return daoSession;
    }

    @Provides
    @Singleton
    RxSchedulers provideRxSchedulers() {
        return new RxSchedulersImpl();
    }

    @Provides
    @Singleton
    GsonConverter provideGsonConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    @Provides
    @Singleton
    RxMapper provideRxMapper() {
        return new RxMapper();
    }

}
