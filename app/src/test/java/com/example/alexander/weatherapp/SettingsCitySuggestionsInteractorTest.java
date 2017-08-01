package com.example.alexander.weatherapp;


import com.example.alexander.weatherapp.Helpers.InteractorTestHelper;
import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SettingsCitySuggestionsInteractorTest {


    private SettingsCitySuggestionsInteractor interactor;
    private DataProvider.Database.CityRepository cityRepository;

    @Before
    public void beforeEachTest() {
        cityRepository = mock(DataProvider.Database.CityRepository.class);
        interactor = new SettingsCitySuggestionsInteractor(Schedulers.newThread(), Schedulers.io(), cityRepository);
    }

    @Test
    public void lookupValueInDB() throws InterruptedException {
        String prfx = "Tst";
        when(cityRepository.getCityByPrefix(prfx)).thenReturn(Collections.emptyList());
        InteractorTestHelper<List<CityUIModel>> interactorTestHelper = new InteractorTestHelper<>(TestSubscriber.create());
        Parameter<String> p = new Parameter<>();
        p.setItem(prfx);
        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);

        interactorTestHelper.getListener().await();
        verify(cityRepository, times(1)).getCityByPrefix(prfx);
        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValue(listResult -> listResult.getData().equals(Collections.emptyList()));

    }

    @Test
    public void correctMapToCity() throws InterruptedException {
        String prfx = "Tst";
        String name = "Tstable";
        int id = 15;

        List<CityToIDModel> cityToIDModelList = new ArrayList<>();
        cityToIDModelList.add(new CityToIDModel(name, id));

        List<CityUIModel> cityUIModels = new ArrayList<>();
        cityUIModels.add(new CityUIModel(cityToIDModelList.get(0).getCityId(), cityToIDModelList.get(0).getAlias()));

        when(cityRepository.getCityByPrefix(prfx)).thenReturn(cityToIDModelList);

        InteractorTestHelper<List<CityUIModel>> interactorTestHelper = new InteractorTestHelper<>(TestSubscriber.create());
        Parameter<String> p = new Parameter<>();
        p.setItem(prfx);
        interactor.execute(p, interactorTestHelper::onNext, interactorTestHelper::onThrowable, interactorTestHelper::onComplete);

        interactorTestHelper.getListener().await();
        interactorTestHelper.getListener().assertComplete();
        interactorTestHelper.getListener().assertValue(listResult -> listResult.getData().containsAll(cityUIModels));

    }


}
