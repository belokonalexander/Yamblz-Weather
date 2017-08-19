package com.yamblz.voltek.weather.di.modules;

import android.content.Context;

import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.DatabaseRepositoryImpl;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.storage.GsonConverter;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.data.storage.StorageRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 17.08.2017.
 */
@Module
public class RepositoriesModule {

    @Provides
    @Singleton
    DatabaseRepository provideDatabaseRepository(DaoSession daoSession) {
        return new DatabaseRepositoryImpl(daoSession);
    }

    @Provides
    @Singleton
    StorageRepository provideStorageRepository(Context context, GsonConverter gsonConverter) {
        return new StorageRepositoryImpl(context, gsonConverter);
    }

}
