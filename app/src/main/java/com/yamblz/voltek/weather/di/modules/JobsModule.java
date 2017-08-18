package com.yamblz.voltek.weather.di.modules;

import android.content.Context;

import com.evernote.android.job.JobManager;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.platform.jobs.JobWrapper;
import com.yamblz.voltek.weather.data.platform.jobs.WeatherJobCreator;
import com.yamblz.voltek.weather.data.storage.StorageRepository;

import dagger.Module;
import dagger.Provides;


@Module
public class JobsModule {

    @Provides
    JobManager provideJobManager(Context context) {
        return JobManager.create(context);
    }

    @Provides
    WeatherJobCreator provideWeatherJobCreator(WeatherAPI weatherApi, StorageRepository storageRepository, DatabaseRepository databaseRepository) {
        return new WeatherJobCreator(weatherApi, storageRepository, databaseRepository);
    }

    @Provides
    JobWrapper provideJobWrapper(StorageRepository StorageRepository, JobManager jobManager) {
        return new JobWrapper(StorageRepository, jobManager);
    }

}
