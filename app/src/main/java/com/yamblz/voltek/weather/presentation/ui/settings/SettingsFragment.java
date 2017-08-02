package com.yamblz.voltek.weather.presentation.ui.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.di.modules.SettingsModule;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BasePreferenceFragment;
import com.yamblz.voltek.weather.presentation.ui.views.SimpleArrayAdapter;
import com.yamblz.voltek.weather.utils.StringUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class SettingsFragment extends BasePreferenceFragment implements SettingsView {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
/*
    @BindView(R.id.city_name_settings_view)
    ClosableAutoCompleteTextView autoCompleteTextView;*/

    private SimpleArrayAdapter<CityUIModel> arrayAdapter;
    private Preference cityPreference;


    @Inject
    @InjectPresenter
    SettingsPresenter presenter;

    @ProvidePresenter()
    SettingsPresenter provideSettingsPresenter() {
        return presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        WeatherApp.get(getContext()).getAppComponent().plus(new SettingsModule()).inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(StorageRepository.CATEGORY_SETTINGS);
        setPreferencesFromResource(R.xml.app_prefs, rootKey);
        initLogic();
    }

    private void initLogic() {
       cityPreference = findPreference("city_preference");
        if(cityPreference!=null) {
            cityPreference.setOnPreferenceClickListener(preference -> {
                Toast.makeText(getContext(),"lala la", Toast.LENGTH_LONG).show();
                return true;
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initToolbar(getString(R.string.title_settings));
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.title_settings);

        ///autoCompleteTextView.setOnKeyActionListener(() -> presenter.selectCity(autoCompleteTextView.getText().toString()));
    }



    @Override
    public void attachInputListeners() {

       /* Disposable getCities = RxTextView.afterTextChangeEvents(autoCompleteTextView)
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
                );*/


        //compositeDisposable.addAll(getCities, selectCities);
    }

    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }

    @Override
    public void showSuggestions(List<CityUIModel> suggestions) {

        /*if (arrayAdapter == null) {
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
            else autoCompleteTextView.post(() -> autoCompleteTextView.showDropDown());*/
    }


    @Override
    public void setCity(String cityName) {
       /* autoCompleteTextView.setText(cityName);
        autoCompleteTextView.clearFocus();*/
       cityPreference.setSummary(cityName);
    }


    @Override
    public void showError(@NonNull Throwable error) {
        toast(getString(StringUtils.fromError(error)));
    }


}
