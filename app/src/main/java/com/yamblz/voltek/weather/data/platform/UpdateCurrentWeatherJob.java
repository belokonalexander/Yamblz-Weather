package com.yamblz.voltek.weather.data.platform;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import java.util.concurrent.TimeUnit;

public class UpdateCurrentWeatherJob extends Job {

    public static final String TAG = "UpdateCurrentWeatherJob";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        Intent serviceIntent = new Intent(getContext(), UpdateCurrentWeatherService.class);
        getContext().startService(serviceIntent);

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
