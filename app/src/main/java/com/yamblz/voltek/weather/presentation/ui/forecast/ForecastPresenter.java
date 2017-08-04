package com.yamblz.voltek.weather.presentation.ui.forecast;

import com.arellomobile.mvp.InjectViewState;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.utils.LogUtils;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    private ForecastInteractor interactor;
    private RxSchedulers rxSchedulers;

    public ForecastPresenter(ForecastInteractor interactor, RxSchedulers rxSchedulers) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
    }

    Disposable loadWeatherTask;

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadWeather(false);
    }

    // View callbacks
    public void notifyRefresh() {
        loadWeather(true);
    }


    private void loadWeather(boolean refresh) {

        if (loadWeatherTask == null || loadWeatherTask.isDisposed()) {
            getViewState().showLoading(true);
            loadWeatherTask = interactor.getCurrentWeather(refresh)
                    .compose(rxSchedulers.getIOToMainTransformerSingle())
                    .doAfterTerminate(() -> getViewState().showLoading(false))
                    .subscribe(weatherUIModel -> {
                        getViewState().showData(weatherUIModel);
                    }, throwable -> {
                        LogUtils.log("error: ", throwable);
                        getViewState().showError(throwable);
                    });

            compositeDisposable.addAll(loadWeatherTask);
        }

    }
}
