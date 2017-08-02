package com.yamblz.voltek.weather.di.components;

import com.yamblz.voltek.weather.di.modules.SettingsModule;
import com.yamblz.voltek.weather.di.scopes.SettingsScope;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsPresenter;
import com.yamblz.voltek.weather.presentation.ui.settings.SelectCity.SettingsSelectCityPresenter;

import dagger.Subcomponent;

/**
 * Created on 31.07.2017.
 */
@Subcomponent(modules = SettingsModule.class)
@SettingsScope
public interface SettingsComponent {

    SettingsSelectCityPresenter getSettingsSelectCityPresenter();

    SettingsPresenter getSettingsPresenter();

}
