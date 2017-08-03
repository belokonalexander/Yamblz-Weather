package com.yamblz.voltek.weather.presentation.ui.forecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
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
import com.yamblz.voltek.weather.utils.StringUtils;
import com.yamblz.voltek.weather.utils.WeatherUtils;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ForecastFragment extends BaseFragment implements ForecastView {



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

    @BindView(R.id.tv_city_name)
    TextView cityNameTv;


    @InjectPresenter()
    ForecastPresenter presenter;

    @ProvidePresenter()
    ForecastPresenter provideForecastPresenter() {
        return WeatherApp.get(getContext()).getAppComponent().plus(new ForecastModule()).getForecastPresenter();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public int getLayout() {
        return R.layout.fragment_forecast;
    }

    @Override
    public int getTitle() {
        return R.string.title_forecast;
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
    public void showData(@Nullable WeatherUIModel weather) {
        if (weather == null) {
            contentContainer.setVisibility(GONE);
        } else {

            weatherIconIm.setImageResource(WeatherUtils.getImageByCondition(weather.getConditionId()));
            descriptionTv.setText(weather.getCondition());
            temperatureTv.setText(getString(R.string.wthr_temperature, weather.getTemperature()));
            humidityTv.setText(getString(R.string.wthr_humidity, weather.getHumidity()));
            cityNameTv.setText(weather.getCityName());
            contentContainer.setVisibility(VISIBLE);
        }
    }

    @Override
    public void showError(@Nullable Throwable error) {

        if (error == null) {
            emptyStateTv.setVisibility(GONE);
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


}
