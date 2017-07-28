package com.example.alexander.weatherapp;


import com.example.alexander.weatherapp.Helpers.InteractorTestHelper;
import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;
import com.yamblz.voltek.weather.domain.interactor.SettingsSetCityInteractor;

import org.greenrobot.greendao.DaoException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsSetCityInteractorTest {


    private SettingsSetCityInteractor interactor;
    private DataProvider.DataBase.CityRepository db;
    private DataProvider.API.Weather api;
    private DataProvider.Storage.Weather storage;

    private InteractorTestHelper<CityUIModel> interactorTestHelper;



    @Before
    public void beforeEachTest() {
        db = mock(DataProvider.DataBase.CityRepository.class);
        api = mock(DataProvider.API.Weather.class);
        storage = mock(DataProvider.Storage.Weather.class);
        interactor = new SettingsSetCityInteractor(Schedulers.newThread(), Schedulers.io(), api, storage, db);
        interactorTestHelper = new InteractorTestHelper<>(TestSubscriber.create());
    }

    @Test
    public void parameterIsOnlyNameWhenAPIAvailable() throws InterruptedException, RequestFailedException, NoConnectionException {

        CityUIModel parameter = new CityUIModel("Test");

        int id = 101;
        CityUIModel expected = new CityUIModel(id, "Test");

        Parameter<CityUIModel> p = new Parameter<>();
        p.setItem(parameter);

        when(api.getCity(parameter.name)).thenReturn(expected);
        CityToIDModel mappedDBValue = new CityToIDModel(expected.name, expected.id);
        when(db.getCityByName(parameter.name)).thenThrow(new DaoException("no values"));
        when(db.saveCity(mappedDBValue)).thenReturn(mappedDBValue);
        when(storage.putSelectedCity(expected)).thenReturn(expected);

        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);


        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValue(cityUIModelResult -> cityUIModelResult.getData().equals(expected));

        verify(db, times(1)).getCityByName(Mockito.any());
        verify(api, times(1)).getCity(Mockito.any());
        verify(db, times(1)).saveCity(Mockito.any());
        verify(storage, times(1)).putSelectedCity(Mockito.any());
    }

    @Test
    public void parameterIsOnlyNameWhenDBContainsIt() throws InterruptedException, RequestFailedException, NoConnectionException {

        CityUIModel parameter = new CityUIModel("Test");

        int id = 101;
        CityUIModel expected = new CityUIModel(id, "Test");

        Parameter<CityUIModel> p = new Parameter<>();
        p.setItem(parameter);

        when(api.getCity(parameter.name)).thenReturn(expected);
        CityToIDModel mappedDBValue = new CityToIDModel(expected.name, expected.id);
        when(db.getCityByName(parameter.name)).thenReturn(mappedDBValue);
        when(db.saveCity(mappedDBValue)).thenReturn(mappedDBValue);
        when(storage.putSelectedCity(expected)).thenReturn(expected);

        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);


        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValue(cityUIModelResult -> cityUIModelResult.getData().equals(expected));

        verify(api, times(0)).getCity(Mockito.any());
        verify(db, times(0)).saveCity(Mockito.any());
        verify(db, times(1)).getCityByName(Mockito.any());
        verify(storage, times(1)).putSelectedCity(Mockito.any());
    }


    @Test
    public void exceptionWhenDBMissAndApiIsNotAvialable() throws InterruptedException, RequestFailedException, NoConnectionException {

        CityUIModel parameter = new CityUIModel("Test");

        int id = 101;
        CityUIModel expected = new CityUIModel(id, "Test");

        Parameter<CityUIModel> p = new Parameter<>();
        p.setItem(parameter);

        when(api.getCity(parameter.name)).thenThrow(new RequestFailedException());
        CityToIDModel mappedDBValue = new CityToIDModel(expected.name, expected.id);
        when(db.getCityByName(parameter.name)).thenThrow(new DaoException("no values"));
        when(db.saveCity(mappedDBValue)).thenReturn(mappedDBValue);
        when(storage.putSelectedCity(expected)).thenReturn(expected);

        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);


        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertNotComplete();

        verify(api, times(1)).getCity(Mockito.any());
        verify(db, times(1)).getCityByName(Mockito.any());
        verify(db, times(0)).saveCity(Mockito.any());
        verify(storage, times(0)).putSelectedCity(Mockito.any());
    }

    @Test
    public void updateCityWhenItWithID() throws RequestFailedException, NoConnectionException, InterruptedException {

        int id = 101;

        CityUIModel parameter = new CityUIModel( id, "Test");

        Parameter<CityUIModel> p = new Parameter<>();
        p.setItem(parameter);


        when(storage.putSelectedCity(parameter)).thenReturn(parameter);
        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);

        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertComplete();
        verify(storage, times(1)).putSelectedCity(Mockito.any());
    }

    @Test
    public void parameterIsNull() throws RequestFailedException, NoConnectionException, InterruptedException {

        CityUIModel parameter = null;

        Parameter<CityUIModel> p = new Parameter<>();
        p.setItem(parameter);

        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);

        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertNoValues();
        interactorTestHelper.getListener().assertComplete();

    }

}
