package com.yamblz.voltek.weather.presentation.ui.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.view.RxView;
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
import com.yamblz.voltek.weather.presentation.ui.views.CustomEditText;

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

    @BindView(R.id.filter_edit_text)
    CustomEditText filterEditText;

    @BindView(R.id.cities_recycler)
    RecyclerView citiesRecyclerView;

    @BindView(R.id.clear_button)
    ImageView clearButton;

    @InjectPresenter
    SettingsSelectCityPresenter presenter;

    private CityAdapter cityAdapter;

    @ProvidePresenter
    public SettingsSelectCityPresenter provideSettingsPresenter() {
        return WeatherApp.get(getContext()).getAppComponent().plus(new SettingsModule()).getSettingsSelectCityPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityAdapter = new CityAdapter(getLayoutInflater(), new ArrayList<>(), this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        citiesRecyclerView.setLayoutManager(llm);
        citiesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), llm.getOrientation()));
        citiesRecyclerView.setAdapter(cityAdapter);

        filterEditText.setOnKeyActionListener(text -> presenter.selectCity(text));

    }

    @Override
    public void attachInputListeners() {

        Disposable getCities = RxTextView.afterTextChangeEvents(filterEditText)
                .skip(1)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.view().isFocused())
                .map(textViewAfterTextChangeEvent -> textViewAfterTextChangeEvent.view().getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> presenter.findSuggestions(text));

        Disposable clearText = RxView.clicks(clearButton).subscribe(o -> presenter.clearTextView());


        compositeDisposable.addAll(getCities, clearText);
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_settings_select_city;
    }

    @Override
    public int getTitle() {
        return R.string.title_city_select;
    }


    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }

    @Override
    public void showSuggestions(List<CityAdapterItem> values) {
        cityAdapter.rewriteItems(values);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectCity(CityUIModel city) {
        Toast.makeText(getContext(), getResources().getString(R.string.saved_as_favorite), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void showError(@NonNull Throwable error) {

    }

    @Override
    public void clearText() {
        filterEditText.setText("");
    }

    @Override
    public void onClick(AdapterItem item) {
        filterEditText.clearFocus();
        CityAdapterItem adapterItem = (CityAdapterItem) item;
        presenter.selectCity(adapterItem.getContent());
    }
}
