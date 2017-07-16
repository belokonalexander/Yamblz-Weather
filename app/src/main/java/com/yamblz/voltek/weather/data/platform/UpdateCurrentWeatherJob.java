package com.yamblz.voltek.weather.data.platform;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.yamblz.voltek.weather.Injector;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class UpdateCurrentWeatherJob extends Job {

    public static final String TAG = "UpdateCurrentWeatherJob";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        CurrentWeatherInteractor interactor = Injector.currentWeatherInteractor();

        Parameter<Void> param = new Parameter<>();
        param.setFlag(CurrentWeatherInteractor.REFRESH);

        interactor.execute(
                param,
                result -> Timber.d("Weather successfully updated in background"),
                Timber::e,
                () -> {}
        );

        return Result.SUCCESS;
    }

    public static void schedulePeriodic(int hours) {
        new JobRequest.Builder(TAG)
                .setPeriodic(TimeUnit.HOURS.toMillis(hours), TimeUnit.MINUTES.toMillis(15))
                .setUpdateCurrent(true)
                .setPersisted(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .build()
                .schedule();
    }
}
