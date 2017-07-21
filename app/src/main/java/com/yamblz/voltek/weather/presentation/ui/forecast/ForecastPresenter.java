package com.yamblz.voltek.weather.presentation.ui.forecast;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;

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
                    if (error instanceof NoConnectionException || error instanceof RequestFailedException) {
                        getViewState().showLoading(false);
                        getViewState().showError(error);
                    } else {
                        throw new Exception("Unhandled exception passed to ForecastPresenter", error);
                    }
                },
                () -> getViewState().showLoading(false)
        );
    }
}
