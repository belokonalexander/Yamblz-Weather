package com.yamblz.voltek.weather.presentation.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.yamblz.voltek.weather.Injector;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.data.platform.UpdateCurrentWeatherJob;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;
import com.yamblz.voltek.weather.presentation.ui.views.ClosableAutoCompleteTextView;
import com.yamblz.voltek.weather.presentation.ui.views.SimpleArrayAdapter;
import com.yamblz.voltek.weather.utils.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class SettingsFragment extends BaseFragment implements SettingsView {

    public static final String INTERVAL_KEY = "INTERVAL_KEY";
    public static final String TAG = "SettingsFragment";

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @InjectPresenter(type = PresenterType.LOCAL, tag = TAG)
    SettingsPresenter presenter;

    @ProvidePresenter(type = PresenterType.LOCAL, tag = TAG)
    SettingsPresenter provideSettingsPresenter() {
        return new SettingsPresenter(Injector.settingsCitySuggestionsInteractor(), Injector.settingsSetCityInteractor(), Injector.currentSettingsInteractor());
    }

    @BindView(R.id.rg_intervals)
    RadioGroup groupIntervals;

    @BindView(R.id.city_name_settings_view)
    ClosableAutoCompleteTextView autoCompleteTextView;

    private SharedPreferences sharedPrefs;
    private SimpleArrayAdapter<CityUIModel> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.title_settings);

        switch (sharedPrefs.getInt(INTERVAL_KEY, 1)) {
            case 1:
                groupIntervals.check(R.id.rb_interval_1);
                break;
            case 3:
                groupIntervals.check(R.id.rb_interval_2);
                break;
            case 5:
                groupIntervals.check(R.id.rb_interval_3);
                break;
        }

        groupIntervals.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_interval_1:
                    changeUpdateInterval(1);
                    break;
                case R.id.rb_interval_2:
                    changeUpdateInterval(3);
                    break;
                case R.id.rb_interval_3:
                    changeUpdateInterval(5);
                    break;
            }
        });

        autoCompleteTextView.setOnKeyActionListener(() -> presenter.selectCity(autoCompleteTextView.getText().toString()));
    }

    private void changeUpdateInterval(int interval) {
        sharedPrefs.edit().putInt(INTERVAL_KEY, interval).apply();
        UpdateCurrentWeatherJob.schedulePeriodic(interval);
    }

    @Override
    public void attachInputListeners() {

        Disposable getCities = RxTextView.afterTextChangeEvents(autoCompleteTextView)
                .skip(1)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.view().isFocused())
                .map(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.view().getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> presenter.findSuggestions(text));


        Disposable selectCities = RxAutoCompleteTextView.itemClickEvents(autoCompleteTextView)
                .map(adapterViewItemClickEvent -> arrayAdapter.getItem(adapterViewItemClickEvent.position()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(city -> presenter.selectCity(city)
                );


        compositeDisposable.addAll(getCities, selectCities);
    }

    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }

    @Override
    public void showSuggestions(List<CityUIModel> suggestions) {

        if (arrayAdapter == null) {
            arrayAdapter = new SimpleArrayAdapter<>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, suggestions);
            autoCompleteTextView.setAdapter(arrayAdapter);
        } else {
            arrayAdapter.clear();
            arrayAdapter.addAll(suggestions);
        }

        if (!autoCompleteTextView.isPopupShowing() && arrayAdapter.getCount() > 0)
            if (autoCompleteTextView.isAttachedToWindow())
                autoCompleteTextView.showDropDown();
            else autoCompleteTextView.post(() -> autoCompleteTextView.showDropDown());
    }



    @Override
    public void setCity(String cityName) {
        autoCompleteTextView.setText(cityName);
        autoCompleteTextView.clearFocus();
    }


    @Override
    public void showError(@NonNull Throwable error) {
        toast(getString(StringUtils.fromError(error)));
    }
}
