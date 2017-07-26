package com.yamblz.voltek.weather;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;
import com.orhanobut.hawk.Hawk;
import com.squareup.leakcanary.LeakCanary;
import com.yamblz.voltek.weather.data.platform.UpdateCurrentWeatherJob;
import com.yamblz.voltek.weather.data.platform.WeatherJobCreator;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsFragment;

import timber.log.Timber;

public class WeatherApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        Hawk.init(this).build();
        Timber.plant(new Timber.DebugTree());

        Injector.init(this);

        Stetho.initializeWithDefaults(this);

        JobManager.create(this).addJobCreator(new WeatherJobCreator());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UpdateCurrentWeatherJob.schedulePeriodic(prefs.getInt(SettingsFragment.INTERVAL_KEY, 1));
    }
}
