package com.yamblz.voltek.weather.presentation.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.di.modules.SettingsModule;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BasePreferenceFragment;
import com.yamblz.voltek.weather.presentation.ui.settings.SelectCity.SelectCityFragment;
import com.yamblz.voltek.weather.presentation.ui.views.SimpleArrayAdapter;

public class SettingsFragment extends BasePreferenceFragment implements SettingsView {


/*
    @BindView(R.id.city_name_settings_view)
    ClosableAutoCompleteTextView autoCompleteTextView;*/

    private SimpleArrayAdapter<CityUIModel> arrayAdapter;
    private Preference cityPreference;


    @InjectPresenter
    SettingsPresenter presenter;

    @ProvidePresenter()
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
        cityPreference = findPreference("city_preference");
        if (cityPreference != null) {
            cityPreference.setOnPreferenceClickListener(preference -> {

                navigationManager.openWithBackStack(new SelectCityFragment(), SettingsFragment.class.getSimpleName());
                return true;
            });
        }
    }

    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }


    @Override
    public void setCitySummary(String cityName) {
        cityPreference.setSummary(cityName);
    }


}
