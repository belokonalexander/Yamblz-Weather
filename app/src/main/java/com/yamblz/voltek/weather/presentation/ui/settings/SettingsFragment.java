package com.yamblz.voltek.weather.presentation.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.di.modules.SettingsModule;
import com.yamblz.voltek.weather.presentation.base.BasePreferenceFragment;
import com.yamblz.voltek.weather.utils.LogUtils;

public class SettingsFragment extends BasePreferenceFragment implements SettingsView, SharedPreferences.OnSharedPreferenceChangeListener {


    @InjectPresenter
    SettingsPresenter presenter;

    @ProvidePresenter
    SettingsPresenter provideSettingsPresenter() {
        return WeatherApp.get(getContext()).getAppComponent().plus(new SettingsModule()).getSettingsPresenter();
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(StorageRepository.CATEGORY_SETTINGS);
        setPreferencesFromResource(R.xml.app_prefs, rootKey);
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public int getTitle() {
        return R.string.nav_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.title_settings);
    }


    @Override
    public void attachInputListeners() {

    }

    @Override
    public void detachInputListeners() {

    }

    @Override
    public void jobStateChanged() {
        LogUtils.logJob("Job state changed!");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        LogUtils.logJob("onSharedPreferenceChanged ---> ");
        if(key.equals(StorageRepository.ENABLE_UPDATE_KEY) || key.equals(StorageRepository.UPDATE_INTERVAL_KEY)) {
            presenter.updateWeatherJob();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
