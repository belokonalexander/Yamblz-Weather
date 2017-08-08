package com.yamblz.voltek.weather.presentation.ui.forecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.di.modules.ForecastModule;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;
import com.yamblz.voltek.weather.presentation.ui.adapter.adapters.ForecastAdapter;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.ForecastAdapterItem;
import com.yamblz.voltek.weather.utils.StringUtils;
import com.yamblz.voltek.weather.utils.WeatherUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class ForecastFragment extends BaseFragment implements ForecastView {

    public static final String TITLE_ARG = "TITLE";

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.ll_content)
    LinearLayout contentContainer;

    @BindView(R.id.tv_empty_state)
    TextView emptyStateTv;

    @BindView(R.id.im_weather_icon)
    ImageView weatherIconIm;

    @BindView(R.id.tv_description)
    TextView descriptionTv;

    @BindView(R.id.tv_temperature)
    TextView temperatureTv;

    @BindView(R.id.tv_humidity)
    TextView humidityTv;

    @BindView(R.id.forecast_recycler)
    RecyclerView forecastRecycler;

    @InjectPresenter()
    ForecastPresenter presenter;

    private ForecastAdapter forecastAdapter;

    @ProvidePresenter()
    ForecastPresenter provideForecastPresenter() {
        return WeatherApp.get(getContext()).getAppComponent().plus(new ForecastModule()).getForecastPresenter();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_forecast;
    }

    @Override
    public int getTitle() {
        return -1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        forecastAdapter = new ForecastAdapter(getLayoutInflater(), new ArrayList<>());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        forecastRecycler.setLayoutManager(llm);
        forecastRecycler.addItemDecoration(new DividerItemDecoration(getContext(),llm.getOrientation()));
        forecastRecycler.setAdapter(forecastAdapter);
    }

    @Override
    public void attachInputListeners() {
        Disposable refreshListener = RxSwipeRefreshLayout.refreshes(swipeContainer)
                .subscribe(o -> presenter.notifyRefresh());

        compositeDisposable.addAll(refreshListener);
    }

    @Override
    public void detachInputListeners() {
        compositeDisposable.clear();
    }

    @Override
    public void showLoading(boolean show) {
        swipeContainer.setRefreshing(show);
    }


    @Override
    public void showData(@Nullable WeatherUIModel currentWeather, @Nullable List<ForecastAdapterItem> weather) {
        if (currentWeather == null) {
            contentContainer.setVisibility(GONE);
        } else {
            emptyStateTv.setVisibility(INVISIBLE);
            contentContainer.setVisibility(VISIBLE);
            weatherIconIm.setImageResource(WeatherUtils.getImageByCondition(currentWeather.getConditionId()));
            descriptionTv.setText(currentWeather.getCondition());
            temperatureTv.setText(getString(R.string.wthr_temperature, currentWeather.getTemperature()));
            humidityTv.setText(getString(R.string.wthr_humidity, currentWeather.getHumidity()));

            forecastAdapter.tryUpdateContent(weather);

        }
    }


    @Override
    public void showError(@Nullable Throwable error) {

        if (error == null) {
            emptyStateTv.setVisibility(INVISIBLE);
        } else {
            String message = getString(StringUtils.fromError(error));

            if (contentContainer.getVisibility() == VISIBLE) {
                toast(message);
            } else {
                emptyStateTv.setText(message);
                emptyStateTv.setVisibility(VISIBLE);
            }
        }
    }

    @Override
    public void initTitle() {
        toolbarLoader.initToolbar(getArguments().getString(TITLE_ARG));
    }


}
