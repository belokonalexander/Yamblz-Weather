package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.platform.jobs.JobWrapper;
import com.yamblz.voltek.weather.data.storage.StorageRepository;

import io.reactivex.Completable;

/**
 * Created on 07.08.2017.
 */

public class SettingsInteractor {

    private JobWrapper jobWrapper;
    private StorageRepository storageRepository;

    public SettingsInteractor(JobWrapper jobWrapper, StorageRepository storageRepository) {
        this.jobWrapper = jobWrapper;
        this.storageRepository = storageRepository;
    }

    public Completable updateJob() {
        return storageRepository.getUpdateEnabled()
                .doOnSuccess(enabled -> {
                    if(enabled) {
                        jobWrapper.tryToStartWeatherJob().subscribe();
                    } else {
                        jobWrapper.disableWeatherJob().subscribe();
                    }
                }).toCompletable();
    }
}
