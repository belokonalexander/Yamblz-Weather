package com.yamblz.voltek.weather.interactor;

import com.yamblz.voltek.weather.helper.ApiDataHelper;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.interactor.CitySettingsInteractor;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;

import org.greenrobot.greendao.DaoException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 09.08.2017.
 */

public class CitySettingsInteractorTest {

    WeatherAPI api;
    DatabaseRepository databaseRepository;
    RxMapper rxMapper = new RxMapper();

    CitySettingsInteractor settingsInteractor;

    @Before
    public void beforeEachTest() {
        databaseRepository = mock(DatabaseRepository.class);
        api = mock(WeatherAPI.class);
        settingsInteractor = new CitySettingsInteractor(api, databaseRepository, rxMapper);
    }

    @Test
    public void citySuggestionsOnlyLooksInDB() throws InterruptedException {

        when(databaseRepository.getCityByPrefix(""))
                .thenReturn(Single.just(new ArrayList<>()));

        TestObserver testObserver = TestObserver.create();
        settingsInteractor.getCitySuggestions("")
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertNoErrors();
        testObserver.assertValue(new ArrayList<>());
        verify(databaseRepository, times(1)).getCityByPrefix("");
    }

    @Test
    public void saveCityWithIdSavedDirectly() throws InterruptedException {
        int id = 1;
        String s = "Name";

        CityUIModel cityUIModel = new CityUIModel(id, s);
        TestObserver testObserver = TestObserver.create();

        when(databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(s, id), false)).thenReturn(Completable.complete());
        when(api.byCityName(s, null)).thenReturn(Single.just(ApiDataHelper.getModel(WeatherResponseModel.class)));
        when(databaseRepository.saveCity(new CityToIDModel(s, id))).thenReturn(Completable.complete());
        when(databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(s, id), true)).thenReturn(Completable.complete());
        when(databaseRepository.getCityByName(s)).thenReturn(Single.just(new CityToIDModel(s, id)));

        settingsInteractor.saveCity(cityUIModel).subscribe(testObserver);

        testObserver.await();
        testObserver.assertNoErrors();
        verify(databaseRepository, times(1)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(0)).saveCity(any());
        verify(databaseRepository, times(0)).getCityByName(any());
        verify(api, times(0)).byCityName(any(), any());
    }

    @Test
    public void saveCityWithoutIdAndContainInDB() throws InterruptedException {
        int id = 1;
        String s = "Name";

        CityUIModel cityUIModel = new CityUIModel(s);
        TestObserver testObserver = TestObserver.create();

        when(databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(s, id), false)).thenReturn(Completable.complete());
        when(api.byCityName(s, null)).thenReturn(Single.just(ApiDataHelper.getModel(WeatherResponseModel.class)));
        when(databaseRepository.saveCity(new CityToIDModel(s, id))).thenReturn(Completable.complete());
        when(databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(s, id), true)).thenReturn(Completable.complete());
        when(databaseRepository.getCityByName(s)).thenReturn(Single.just(new CityToIDModel(s, id)));

        settingsInteractor.saveCity(cityUIModel).subscribe(testObserver);

        testObserver.await();
        testObserver.assertNoErrors();
        verify(databaseRepository, times(1)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(0)).saveCity(any());
        verify(databaseRepository, times(1)).getCityByName(any());
        verify(api, times(0)).byCityName(any(), any());
    }

    @Test
    public void saveCityWithoutIdAndContainInApi() throws InterruptedException {
        int id = 1;
        String s = "Name";

        CityUIModel cityUIModel = new CityUIModel(s);
        TestObserver testObserver = TestObserver.create();

        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());
        when(api.byCityName(s, null)).thenReturn(Single.just(ApiDataHelper.getModel(WeatherResponseModel.class)));
        when(databaseRepository.saveCity(any(CityToIDModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());
        when(databaseRepository.getCityByName(s)).thenReturn(Single.error(new DaoException()));

        settingsInteractor.saveCity(cityUIModel).subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        verify(databaseRepository, times(1)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(1)).saveCity(any());
        verify(databaseRepository, times(1)).getCityByName(any());
        verify(api, times(1)).byCityName(any(), any());
    }

    @Test
    public void saveCityWithoutIdAndApiIsDisabled() throws InterruptedException {
        int id = 1;
        String s = "Name";

        CityUIModel cityUIModel = new CityUIModel(s);
        TestObserver testObserver = TestObserver.create();

        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());
        when(api.byCityName(s, null)).thenReturn(Single.error(new NoConnectionException()));
        when(databaseRepository.saveCity(any(CityToIDModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());
        when(databaseRepository.getCityByName(s)).thenReturn(Single.error(new DaoException()));

        settingsInteractor.saveCity(cityUIModel).subscribe(testObserver);

        testObserver.await();
        testObserver.assertNotComplete();
        verify(databaseRepository, times(0)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(0)).saveCity(any());
        verify(databaseRepository, times(1)).getCityByName(any());
        verify(api, times(1)).byCityName(any(), any());
    }



}
