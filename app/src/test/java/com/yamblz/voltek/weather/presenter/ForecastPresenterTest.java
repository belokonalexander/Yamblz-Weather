package com.yamblz.voltek.weather.presenter;

import com.yamblz.voltek.weather.helper.ApiDataHelper;
import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastPresenter;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastView$$State;
import com.yamblz.voltek.weather.utils.rx.RxSchedulers;
import com.yamblz.voltek.weather.utils.rx.RxSchedulersTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 10.08.2017.
 */

public class ForecastPresenterTest {


    private ForecastPresenter forecastPresenter;

    @Mock
    ForecastView$$State presenterViewState;

    @Mock
    ForecastInteractor forecastInteractor;

    private RxSchedulers rxSchedulers = new RxSchedulersTest();

    private List<WeatherUIModel> dbData;
    private List<WeatherUIModel> apiData;
    private WeatherUIModel dummyDataItem;

    @Before
    public void beforeEachTest() {
        MockitoAnnotations.initMocks(this);
        forecastPresenter = new ForecastPresenter(forecastInteractor, rxSchedulers);
        forecastPresenter.setViewState(presenterViewState);

        dummyDataItem = new WeatherUIModel(ApiDataHelper.getModel(WeatherResponseModel.class));

    }

    @Test
    public void onFirstViewAttachShouldLoadWeatherAndInitTitle() throws InterruptedException {

        dbData = new ArrayList<>(Arrays.asList(dummyDataItem, dummyDataItem));
        apiData = new ArrayList<>(Arrays.asList(dummyDataItem, dummyDataItem));

        when(forecastInteractor.getCurrentWeather(anyBoolean())).thenReturn(Observable.just(dbData, apiData));

        forecastPresenter.onFirstViewAttach();

        verify(presenterViewState, times(1)).initTitle();
        verify(presenterViewState, times(2)).showData(any(), anyList());
    }

    @Test
    public void loadWeatherIfOnlyDBData() throws InterruptedException {

        dbData = new ArrayList<>(Arrays.asList(dummyDataItem, dummyDataItem));

        when(forecastInteractor.getCurrentWeather(anyBoolean()))
                .thenReturn(Observable.concat(Observable.just(dbData), Observable.error(new NoConnectionException())));

        forecastPresenter.onFirstViewAttach();

        InOrder order = Mockito.inOrder(presenterViewState);

        verify(presenterViewState, times(1)).initTitle();
        order.verify(presenterViewState, times(1)).showData(any(), anyList());
        order.verify(presenterViewState, times(1)).showError(any());
    }

    @Test
    public void loadWeatherIfOnlyInApi() throws InterruptedException {

        apiData = new ArrayList<>(Arrays.asList(dummyDataItem, dummyDataItem));

        when(forecastInteractor.getCurrentWeather(anyBoolean()))
                .thenReturn(Observable.just(apiData));

        forecastPresenter.onFirstViewAttach();

        verify(presenterViewState, times(1)).initTitle();
        verify(presenterViewState, times(0)).showError(any());
        verify(presenterViewState, times(1)).showData(any(), anyList());

    }

    @Test
    public void loadWeatherIfOneItemSuccess() throws InterruptedException {

        apiData = new ArrayList<>(Collections.singletonList(dummyDataItem));

        when(forecastInteractor.getCurrentWeather(anyBoolean()))
                .thenReturn(Observable.just(apiData));

        forecastPresenter.notifyRefresh();

        verify(presenterViewState, times(0)).showError(any());
        verify(presenterViewState, times(1)).showData(any(), anyList());

    }

    @Test
    public void loadWeatherIfEmptyDataFailure() throws InterruptedException {

        apiData = new ArrayList<>();

        when(forecastInteractor.getCurrentWeather(anyBoolean()))
                .thenReturn(Observable.just(apiData));

        forecastPresenter.notifyRefresh();

        verify(presenterViewState, times(1)).showError(any());
        verify(presenterViewState, times(0)).showData(any(), anyList());

    }

}
