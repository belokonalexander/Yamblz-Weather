package com.yamblz.voltek.weather.presentation.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.yamblz.voltek.weather.Injector;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.data.platform.UpdateCurrentWeatherJob;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;
import com.yamblz.voltek.weather.presentation.ui.views.AutoCompletableCustom;

import java.util.List;

import butterknife.BindView;
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
        return new SettingsPresenter(Injector.settingsInteractor());
    }

    @BindView(R.id.rg_intervals)
    RadioGroup groupIntervals;

    @BindView(R.id.city_name_settings_view)
    AutoCompletableCustom autoCompleteTextView;

    private SharedPreferences sharedPrefs;

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
    }

    private void changeUpdateInterval(int interval) {
        sharedPrefs.edit().putInt(INTERVAL_KEY, interval).apply();
        UpdateCurrentWeatherJob.schedulePeriodic(interval);
    }

    @Override
    public void attachInputListeners() {
        Disposable getCities = RxTextView.afterTextChangeEvents(autoCompleteTextView)
                .skipInitialValue()
                .subscribe(text -> presenter.findSuggestions(text.view().getText().toString()));

        compositeDisposable.add(getCities);
    }

    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }

    @Override
    public void showSuggestions(List<String> strings) {
        Log.e("TAG", " ----> " + strings);
    }
}
