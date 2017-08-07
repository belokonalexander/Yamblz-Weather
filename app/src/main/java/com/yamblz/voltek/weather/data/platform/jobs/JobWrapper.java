package com.yamblz.voltek.weather.data.platform.jobs;

import com.evernote.android.job.JobManager;
import com.yamblz.voltek.weather.data.storage.StorageRepository;

import io.reactivex.Completable;


public class JobWrapper {

    private StorageRepository storageRepository;
    private JobManager jobManager;

    public JobWrapper(StorageRepository storageRepository, JobManager jobManager) {
        this.storageRepository = storageRepository;
        this.jobManager = jobManager;
    }

    public Completable tryToStartWeatherJob() {
        return storageRepository.getUpdateInterval()
                .doOnSuccess(WeatherJob::scheduleJob).toCompletable();

    }

    public Completable disableWeatherJob() {
        return Completable.fromAction(() -> jobManager.cancelAllForTag(WeatherJob.TAG));
    }
}
