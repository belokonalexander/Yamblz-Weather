package com.yamblz.voltek.weather;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.JobManager;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.yamblz.voltek.weather.data.database.AppDatabaseHelper;
import com.yamblz.voltek.weather.data.database.models.DaoMaster;
import com.yamblz.voltek.weather.data.database.models.DaoSession;
import com.yamblz.voltek.weather.data.platform.jobs.WeatherJobCreator;
import com.yamblz.voltek.weather.di.components.AppComponent;
import com.yamblz.voltek.weather.di.components.DaggerAppComponent;
import com.yamblz.voltek.weather.di.modules.AppModule;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;

public class WeatherApp extends Application {

    private AppComponent appComponent;

    @Inject
    WeatherJobCreator weatherJobCreator;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        DaoMaster.DevOpenHelper helper = new AppDatabaseHelper(this, "weather.db", null);
        Database db = helper.getWritableDb();
        DaoSession session = new DaoMaster(db).newSession();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this, session)).build();
        appComponent.inject(this);

        Stetho.initializeWithDefaults(this);

        JobManager.create(this).addJobCreator(weatherJobCreator);

    }

    @NonNull
    public static WeatherApp get(@NonNull Context context) {
        return (WeatherApp) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
