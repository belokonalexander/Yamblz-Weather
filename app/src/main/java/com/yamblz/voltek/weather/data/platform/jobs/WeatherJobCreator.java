package com.yamblz.voltek.weather.data.platform.jobs;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.storage.StorageRepository;


public class WeatherJobCreator implements JobCreator {


    private final WeatherAPI weatherApi;
    private final StorageRepository storageRepository;
    private final DatabaseRepository databaseRepository;


    public WeatherJobCreator(WeatherAPI weatherApi, StorageRepository storageRepository, DatabaseRepository databaseRepository) {
        this.weatherApi = weatherApi;
        this.databaseRepository = databaseRepository;
        this.storageRepository = storageRepository;
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case WeatherJob.TAG:
                //for error fix: "Job for tag GET_WEATHER_JOB was already run, a creator should always create a new Job instance"
                return new WeatherJob(weatherApi, storageRepository, databaseRepository);
            default:
                return null;
        }

    }
}
