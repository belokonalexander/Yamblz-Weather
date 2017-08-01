package com.yamblz.voltek.weather.data.platform.jobs;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.storage.StorageRepository;


public class WeatherJobCreator implements JobCreator {


    private WeatherAPI weatherApi;
    private StorageRepository storageRepository;


    public WeatherJobCreator(WeatherAPI weatherApi, StorageRepository storageRepository) {
        this.weatherApi = weatherApi;
        this.storageRepository = storageRepository;
    }

    @Override
    public Job create(String tag) {
        switch (tag) {
            case WeatherJob.TAG:
                //error fix Job for tag GET_WEATHER_JOB was already run, a creator should always create a new Job instance
                return new WeatherJob(weatherApi, storageRepository);
            default:
                return null;
        }

    }
}
