package com.yamblz.voltek.weather.di.components;

import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.di.modules.AppModule;
import com.yamblz.voltek.weather.di.modules.ForecastModule;
import com.yamblz.voltek.weather.di.modules.JobsModule;
import com.yamblz.voltek.weather.di.modules.NetworkModule;
import com.yamblz.voltek.weather.di.modules.SettingsModule;
import com.yamblz.voltek.weather.presentation.ui.main.WeatherPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on 31.07.2017.
 */
@Component(modules = {AppModule.class, NetworkModule.class, JobsModule.class})
@Singleton
public interface AppComponent {

    SettingsComponent plus(SettingsModule settingsModule);

    ForecastComponent plus(ForecastModule forecastModule);

    void inject(WeatherApp app);

    WeatherPresenter getFavoritesPresenter();

}
