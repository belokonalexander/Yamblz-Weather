package com.yamblz.voltek.weather.data.platform.jobs;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.utils.LogUtils;

import java.util.concurrent.TimeUnit;


class WeatherJob extends Job {

    static final String TAG = "GET_WEATHER_JOB";


    private final WeatherAPI api;
    private final DatabaseRepository databaseRepository;
    private final StorageRepository storageRepository;

    WeatherJob(WeatherAPI weatherApi, StorageRepository storageRepository, DatabaseRepository databaseRepository) {
        this.api = weatherApi;
        this.databaseRepository = databaseRepository;
        this.storageRepository = storageRepository;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        final boolean[] flag = {false};

        storageRepository.getSelectedCity()
                .zipWith(storageRepository.getUnitsSettings(), (cityUIModel, s) -> {
                    databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(cityUIModel.name, cityUIModel.id), false).subscribe();
                    return new Pair<>(cityUIModel, s);
                }).flatMap(pair -> api.forecastById(pair.first.id, pair.second))
                .zipWith(storageRepository.getSelectedCity(), (forecastResponseModel, cityUIModel) -> {
                    forecastResponseModel.city.name = cityUIModel.name;
                    return forecastResponseModel;
                }).subscribe(forecastResponseModel -> {
            flag[0] = true;
            databaseRepository.updateFavorite(forecastResponseModel).subscribe();
        }, throwable -> flag[0] = false);

        LogUtils.logJob("Job result: " + flag[0]);

        return flag[0] ? Result.SUCCESS : Result.FAILURE;
    }

    static void scheduleJob(int minutes) {
        int flex = 5;
        new JobRequest.Builder(WeatherJob.TAG)
                //.setRequiresCharging(true)
                .setPersisted(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setUpdateCurrent(true)
                .setPeriodic(TimeUnit.MINUTES.toMillis(minutes), TimeUnit.MINUTES.toMillis(flex))
                .build()
                .schedule();
    }
}
