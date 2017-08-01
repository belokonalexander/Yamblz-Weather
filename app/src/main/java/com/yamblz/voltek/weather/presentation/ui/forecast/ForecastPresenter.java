package com.yamblz.voltek.weather.presentation.ui.forecast;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.utils.LogUtils;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    private ForecastInteractor interactor;
    private RxSchedulers rxSchedulers;

    public ForecastPresenter(ForecastInteractor interactor, RxSchedulers rxSchedulers) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadWeather(false);
    }

    // View callbacks
    public void notifyRefresh() {
        loadWeather(true);
    }

    // Private logic
    private void loadWeather(boolean refresh) {

        getViewState().showLoading(true);


        interactor.getCurrentWeather(refresh)
                .compose(rxSchedulers.getIOToMainTransformerSingle())
                .subscribe(weatherUIModel -> {
                    LogUtils.log(" Получили данные ");
                    getViewState().showData(weatherUIModel);
                    getViewState().showLoading(false);
                }, throwable -> {
                    LogUtils.log(" Не получили данные ", throwable);

                    getViewState().showError(throwable);
                    getViewState().showLoading(false);
                });

    }
}
