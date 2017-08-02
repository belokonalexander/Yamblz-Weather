package com.yamblz.voltek.weather.presentation.ui.settings.SelectCity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.di.modules.SettingsModule;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;
import com.yamblz.voltek.weather.presentation.ui.adapter.adapters.CityAdapter;
import com.yamblz.voltek.weather.presentation.ui.adapter.adapters.OnAdapterItemClickListener;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.AdapterItem;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;
import com.yamblz.voltek.weather.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created on 02.08.2017.
 */

public class SelectCityFragment extends BaseFragment implements SettingsCityView, OnAdapterItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.filter_edit_text)
    EditText filterEditText;

    @BindView(R.id.cities_recycler)
    RecyclerView citiesRecyclerView;

    @InjectPresenter
    SettingsSelectCityPresenter presenter;

    CityAdapter cityAdapter;

    @ProvidePresenter
    public SettingsSelectCityPresenter provideSettingsPresenter() {
        return WeatherApp.get(getContext()).getAppComponent().plus(new SettingsModule()).getSettingsSelectCityPresenter();
    }

    @Override
    public void attachInputListeners() {

        cityAdapter = new CityAdapter(getLayoutInflater(), new ArrayList<>(), this);

        citiesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        citiesRecyclerView.setAdapter(cityAdapter);

        Disposable getCities = RxTextView.afterTextChangeEvents(filterEditText)
                .skip(1)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.view().isFocused())
                .map(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.view().getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> presenter.findSuggestions(text));


        /*Disposable selectCities = RxAutoCompleteTextView.itemClickEvents(autoCompleteTextView)
                .map(adapterViewItemClickEvent -> arrayAdapter.getItem(adapterViewItemClickEvent.position()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(city -> presenter.selectCity(city)
                );*/

        compositeDisposable.addAll(getCities/*, selectCities*/);
    }

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_settings_select_city;
    }

    @Override
    protected int getTitle() {
        return R.string.title_city_select;
    }


    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }

    @Override
    public void showSuggestions(List<CityAdapterItem> values) {
        LogUtils.log(" showSuggestions -> " + values);
        cityAdapter.rewriteItems(values);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectCity(CityUIModel city) {

    }

    @Override
    public void showError(@NonNull Throwable error) {

    }

    @Override
    public void onClick(AdapterItem item) {
        LogUtils.log(" -> " + item);
    }
}