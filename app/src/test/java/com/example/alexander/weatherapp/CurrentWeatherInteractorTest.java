package com.example.alexander.weatherapp;


import com.example.alexander.weatherapp.Helpers.ApiWeatherModelHelper;
import com.example.alexander.weatherapp.Helpers.InteractorTestHelper;
import com.example.alexander.weatherapp.Helpers.InteractorTestHelperObserver;
import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CurrentWeatherInteractorTest {

    private DataProvider.Storage.Weather storage;
    private DataProvider.API.Weather api;
    private ForecastInteractor currentWeatherInteractor;

    @Before
    public void beforeEachTest() {
        storage = mock(DataProvider.Storage.Weather.class);
        api = mock(DataProvider.API.Weather.class);
        currentWeatherInteractor = new ForecastInteractor(Schedulers.newThread(), Schedulers.io(), api, storage);
    }

    @Test
    public void currentWeatherNotInPrefsCall() throws RequestFailedException, NoConnectionException, InterruptedException {
        WeatherUIModel currentWeather = new WeatherUIModel(ApiWeatherModelHelper.getWeatherModel());

        when(api.getCurrentByID(100)).thenReturn(currentWeather);
        when(storage.getSelectedCity()).thenReturn(new CityUIModel(100, "Someland"));
        when(storage.getCurrent()).thenReturn(null);

        InteractorTestHelper<WeatherUIModel> interactorTestHelper = new InteractorTestHelper<>(TestSubscriber.create());

        currentWeatherInteractor.execute(new Parameter<>(),interactorTestHelper::onNext,interactorTestHelper::onThrowable,interactorTestHelper::onComplete);

        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValueCount(1);
        interactorTestHelper.getListener().assertValue(weatherUIModelResult -> weatherUIModelResult.getData().equals(currentWeather));

    }


    @Test
    public void currentWeatherInPrefsCall() throws RequestFailedException, NoConnectionException, InterruptedException {
        WeatherUIModel currentWeather = new WeatherUIModel(ApiWeatherModelHelper.getWeatherModel());

        when(api.getCurrentByID(100)).thenReturn(currentWeather);
        when(storage.getSelectedCity()).thenReturn(new CityUIModel(100, "Someland"));
        when(storage.getCurrent()).thenReturn(currentWeather);

        InteractorTestHelper<WeatherUIModel> interactorTestHelper = new InteractorTestHelper<>(TestSubscriber.create());

        currentWeatherInteractor.execute(new Parameter<>(),interactorTestHelper::onNext,interactorTestHelper::onThrowable,interactorTestHelper::onComplete);


        interactorTestHelper.getListener().await();
        verify(api,times(0)).getCurrentByID(100);
        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValueCount(1);
        interactorTestHelper.getListener().assertValue(weatherUIModelResult -> weatherUIModelResult.getData().equals(currentWeather));

    }

    @Test
    public void currentWeatherInPrefsCallWithRefreshFlag() throws RequestFailedException, NoConnectionException, InterruptedException {
        WeatherUIModel currentWeather = new WeatherUIModel(ApiWeatherModelHelper.getWeatherModel());

        when(api.getCurrentByID(100)).thenReturn(currentWeather);
        when(storage.getSelectedCity()).thenReturn(new CityUIModel(100, "Someland"));
        when(storage.getCurrent()).thenReturn(currentWeather);

        InteractorTestHelper<WeatherUIModel> interactorTestHelper = new InteractorTestHelper<>(TestSubscriber.create());
        Parameter<Void> p = new Parameter<>();
        p.setFlag(ForecastInteractor.REFRESH);
        currentWeatherInteractor.execute(p,interactorTestHelper::onNext,interactorTestHelper::onThrowable,interactorTestHelper::onComplete);


        interactorTestHelper.getListener().await();
        verify(api,times(1)).getCurrentByID(100);

        //ERROR we should go directly to api, why we're looking for value in prefs?
        verify(storage,times(0)).getCurrent();

        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValueCount(1);
        interactorTestHelper.getListener().assertValue(weatherUIModelResult -> weatherUIModelResult.getData().equals(currentWeather));

    }

    @Test
    public void currentWeatherApiError() throws RequestFailedException, NoConnectionException, InterruptedException {

        when(api.getCurrentByID(100)).thenThrow(new NoConnectionException());
        when(storage.getSelectedCity()).thenReturn(new CityUIModel(100, "Someland"));
        when(storage.getCurrent()).thenReturn(null);

        InteractorTestHelperObserver<WeatherUIModel> interactorTestHelper = new InteractorTestHelperObserver<>(TestObserver.create());

        currentWeatherInteractor.execute(new Parameter<>(),interactorTestHelper::onNext,interactorTestHelper::onThrowable,interactorTestHelper::onComplete);

        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertNotComplete();

    }

}
