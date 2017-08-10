package com.yamblz.voltek.weather.presentation.ui.forecast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.Pair;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;
import com.yamblz.voltek.weather.presentation.base.BasePresenter;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.ForecastAdapterItem;
import com.yamblz.voltek.weather.utils.LogUtils;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

@InjectViewState
public class ForecastPresenter extends BasePresenter<ForecastView> {

    private final ForecastInteractor interactor;
    private final RxSchedulers rxSchedulers;
    private Disposable loadWeatherTask;

    public ForecastPresenter(ForecastInteractor interactor, RxSchedulers rxSchedulers) {
        this.interactor = interactor;
        this.rxSchedulers = rxSchedulers;
    }

    @Override
    public void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadWeather(false);
        getViewState().initTitle();
    }

    public void notifyRefresh() {
        loadWeather(true);
    }

    private void loadWeather(boolean refresh) {

        if (loadWeatherTask == null || loadWeatherTask.isDisposed()) {
            //uncomment if should show refresh item on loading
            //getViewState().showLoading(true);
            loadWeatherTask = interactor.getCurrentWeather(refresh)
                    .compose(rxSchedulers.getIOToMainTransformer(true))
                    .doAfterTerminate(() -> getViewState().showLoading(false))
                    .map(weatherUIModels -> {

                        WeatherUIModel current;
                        List<ForecastAdapterItem> forecastAdapterItems = new ArrayList<>();

                        if (!weatherUIModels.isEmpty()) {
                            current = weatherUIModels.get(0);

                            for (int i = 1; i < weatherUIModels.size(); i++) {
                                forecastAdapterItems.add(new ForecastAdapterItem(weatherUIModels.get(i)));
                            }

                        } else throw new RuntimeException("Empty weather data");

                        return new Pair<>(current, forecastAdapterItems);
                    })
                    .subscribe(weatherUIModel -> {
                        getViewState().showData(weatherUIModel.first, weatherUIModel.second);
                    }, throwable -> {
                        LogUtils.log("loadWeather error: ", throwable);
                        getViewState().showError(throwable);
                    });

            compositeDisposable.addAll(loadWeatherTask);
        }

    }
}
