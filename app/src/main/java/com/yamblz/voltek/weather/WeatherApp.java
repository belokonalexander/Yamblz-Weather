package com.yamblz.voltek.weather;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
import com.squareup.leakcanary.LeakCanary;

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
    }
}
