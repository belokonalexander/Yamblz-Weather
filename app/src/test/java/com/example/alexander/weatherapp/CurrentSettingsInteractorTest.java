package com.example.alexander.weatherapp;


import com.example.alexander.weatherapp.Helpers.InteractorTestHelper;
import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.interactor.CurrentSettingsInteractor;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * I think interactor should only return the Observable for data
 * and subsrciption will be in presenter. Because now I can't test interactor's tasks
 * without presenter logic (Alexander)
 */

public class CurrentSettingsInteractorTest {

    private DataProvider.Storage.Weather storage;
    private CurrentSettingsInteractor currentSettingsInteractor;

    @Before
    public void beforeEachTest() {
        storage = mock(DataProvider.Storage.Weather.class);
        currentSettingsInteractor = new CurrentSettingsInteractor(Schedulers.newThread(), Schedulers.io(), storage);
    }

    @Test
    public void cityWasReturn() throws InterruptedException {
        CityUIModel storageValue = new CityUIModel(10, "Somewhere");
        when(storage.getSelectedCity()).thenReturn(storageValue);

        InteractorTestHelper<CityUIModel> testSubscriber = new InteractorTestHelper<>(TestSubscriber.create());

        currentSettingsInteractor.execute(new Parameter<>(), testSubscriber::onNext, testSubscriber::onThrowable, testSubscriber::onComplete);

        verify(storage, times(1)).getSelectedCity();
        testSubscriber.getListener().await();
        testSubscriber.getListener().assertComplete();
        testSubscriber.getListener().assertValue(cityUIModelResult -> cityUIModelResult.getData().equals(storageValue));
    }
}
