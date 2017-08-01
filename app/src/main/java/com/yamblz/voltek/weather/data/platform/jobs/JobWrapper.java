package com.yamblz.voltek.weather.data.platform.jobs;

import android.content.Context;

import com.evernote.android.job.JobManager;
import com.yamblz.voltek.weather.data.storage.StorageRepository;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;


public class JobWrapper {

    private StorageRepository storageRepository;
    private JobManager jobManager;

    public JobWrapper(Context context, StorageRepository storageRepository, JobManager jobManager) {
        this.storageRepository = storageRepository;
        this.jobManager = jobManager;
    }

    public Maybe<Boolean> tryToStartWeatherJob() {
       return storageRepository.getUpdateEnabled().filter(enabled -> enabled)
                .flatMap(new Function<Boolean, MaybeSource<Integer>>() {
                    @Override
                    public MaybeSource<Integer> apply(@NonNull Boolean aBoolean) throws Exception {
                        return storageRepository.getUpdateInterval().toMaybe();
                    }
                }).map(integer -> {
                    WeatherJob.scheduleJob(integer);
                    return true;
                });
    }

    public Completable disableWeatherJob() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                jobManager.cancelAllForTag(WeatherJob.TAG);
            }
        });
    }
}
