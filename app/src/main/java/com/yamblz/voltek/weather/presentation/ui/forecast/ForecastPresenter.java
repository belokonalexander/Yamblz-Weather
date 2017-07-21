package com.yamblz.voltek.weather.presentation.ui.forecast;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;

import timber.log.Timber;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    private CurrentWeatherInteractor interactor;

    public ForecastPresenter(CurrentWeatherInteractor interactor) {
        this.interactor = interactor;
        loadWeather(false);
    }

    // View callbacks
    public void notifyRefresh() {
        loadWeather(true);
    }

    // Private logic
    private void loadWeather(boolean refresh) {
        Parameter<Void> param = new Parameter<>();
        if (refresh)
            param.setFlag(CurrentWeatherInteractor.REFRESH);

        getViewState().showLoading(true);
        getViewState().showError(null);

        interactor.execute(
                param,
                result -> getViewState().showData(result.getData()),
                error -> {
                    Timber.e(error);

                    getViewState().showLoading(false);
                    getViewState().showError(error);
                },
                () -> getViewState().showLoading(false)
        );
    }
}
