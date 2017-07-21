package com.yamblz.voltek.weather.data.platform;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class WeatherJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case UpdateCurrentWeatherJob.TAG:
                return new UpdateCurrentWeatherJob();
            default:
                return null;
        }
    }
}