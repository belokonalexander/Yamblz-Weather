package com.yamblz.voltek.weather.interactor;

import com.yamblz.voltek.weather.helper.ApiDataHelper;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.interactor.ForecastInteractor;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on 09.08.2017.
 */

public class ForecastInteractorTest {


    ForecastInteractor forecastInteractor;
    WeatherAPI api;
    StorageRepository storageRepository;
    DatabaseRepository databaseRepository;
    RxMapper rxMapper = new RxMapper();

    @Before
    public void beforeEachTest() {

        api = mock(WeatherAPI.class);
        storageRepository = mock(StorageRepository.class);
        databaseRepository = mock(DatabaseRepository.class);

        forecastInteractor = new ForecastInteractor(api, storageRepository, databaseRepository, rxMapper);
    }

    @Test
    public void getSelectedCityNameIsFromSharedPrefs() throws InterruptedException {
        String name = "name";
        int id = 1;
        when(storageRepository.getSelectedCity()).thenReturn(Single.just(new CityUIModel(id, name)));

        TestObserver testObserver = TestObserver.create();

        forecastInteractor.getSelectedCityName().subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValue(name);
        verify(storageRepository, times(1)).getSelectedCity();
    }

    @Test
    public void getCurrentWeatherWhenRefreshAndApiIsAvailable() throws InterruptedException {

        String name = "name";
        int id = 1;
        String units = "RU";

        CityUIModel selected = new CityUIModel(id, name);
        CityToIDModel selectedId = new CityToIDModel(name, id);

        when(storageRepository.getSelectedCity()).thenReturn(Single.just(selected));
        when(storageRepository.getUnitsSettings()).thenReturn(Single.just(units));
        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());

        when(api.forecastById(anyInt(), anyString())).thenReturn(Single.just(ApiDataHelper.getModel(ForecastResponseModel.class)));
        when(databaseRepository.updateFavorite(any(ForecastResponseModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getFavoriteForForecast(id)).thenReturn(Single.just(ApiDataHelper.getModel(ForecastResponseModel.class)));

        TestObserver testObserver = TestObserver.create();

        forecastInteractor.getCurrentWeather(true).subscribe(testObserver);

        testObserver.await();

        testObserver.assertNoErrors();
        testObserver.assertComplete();
        testObserver.assertValueCount(1);

        verify(databaseRepository, times(1)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(0)).getFavoriteForForecast(id);
        verify(api, times(1)).forecastById(id, units);
        verify(databaseRepository, times(1)).updateFavorite(any());
    }

    @Test
    public void getCurrentWeatherWhenRefreshAndApiNotAvailable() throws InterruptedException {

        String name = "name";
        int id = 1;
        String units = "RU";

        CityUIModel selected = new CityUIModel(id, name);
        CityToIDModel selectedId = new CityToIDModel(name, id);

        when(storageRepository.getSelectedCity()).thenReturn(Single.just(selected));
        when(storageRepository.getUnitsSettings()).thenReturn(Single.just(units));
        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());

        when(api.forecastById(anyInt(), anyString())).thenReturn(Single.error(new NoConnectionException()));
        when(databaseRepository.updateFavorite(any(ForecastResponseModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getFavoriteForForecast(id)).thenReturn(Single.just(ApiDataHelper.getModel(ForecastResponseModel.class)));

        TestObserver testObserver = TestObserver.create();

        forecastInteractor.getCurrentWeather(true).subscribe(testObserver);

        testObserver.await();
        testObserver.assertNotComplete();
        testObserver.assertValueCount(0);

        verify(databaseRepository, times(1)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(0)).getFavoriteForForecast(id);
        verify(api, times(1)).forecastById(id, units);
        verify(databaseRepository, times(0)).updateFavorite(any());
    }

    @Test
    public void getCurrentWeatherWhenNotRefreshAndApiNotAvailable() throws InterruptedException {

        String name = "name";
        int id = 1;
        String units = "RU";

        CityUIModel selected = new CityUIModel(id, name);
        CityToIDModel selectedId = new CityToIDModel(name, id);

        when(storageRepository.getSelectedCity()).thenReturn(Single.just(selected));
        when(storageRepository.getUnitsSettings()).thenReturn(Single.just(units));
        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());

        when(api.forecastById(anyInt(), anyString())).thenReturn(Single.error(new NoConnectionException()));
        when(databaseRepository.updateFavorite(any(ForecastResponseModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getFavoriteForForecast(id)).thenReturn(Single.just(ApiDataHelper.getModel(ForecastResponseModel.class)));

        TestObserver testObserver = TestObserver.create();

        forecastInteractor.getCurrentWeather(false).subscribe(testObserver);

        testObserver.await();
        testObserver.assertValueCount(1);
        testObserver.assertNotComplete();


        verify(databaseRepository, times(2)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(1)).getFavoriteForForecast(id);
        verify(api, times(1)).forecastById(id, units);
        verify(databaseRepository, times(0)).updateFavorite(any());
    }


    @Test
    public void getCurrentWeatherWhenNotRefreshAndApiIsAvailable() throws InterruptedException {


        String name = "name";
        int id = 1;
        String units = "RU";

        CityUIModel selected = new CityUIModel(id, name);
        CityToIDModel selectedId = new CityToIDModel(name, id);

        when(storageRepository.getSelectedCity()).thenReturn(Single.just(selected));
        when(storageRepository.getUnitsSettings()).thenReturn(Single.just(units));
        when(databaseRepository.saveAsFavoriteIfNotExists(any(CityToIDModel.class), anyBoolean())).thenReturn(Completable.complete());

        when(api.forecastById(anyInt(), anyString())).thenReturn(Single.just(ApiDataHelper.getModel(ForecastResponseModel.class)));
        when(databaseRepository.updateFavorite(any(ForecastResponseModel.class))).thenReturn(Completable.complete());
        when(databaseRepository.getFavoriteForForecast(id)).thenReturn(Single.just(ApiDataHelper.getModel(ForecastResponseModel.class)));

        TestObserver testObserver = TestObserver.create();

        forecastInteractor.getCurrentWeather(false).subscribe(testObserver);

        testObserver.await();
        testObserver.assertValueCount(2);
        testObserver.assertComplete();

        verify(databaseRepository, times(2)).saveAsFavoriteIfNotExists(any(), anyBoolean());
        verify(databaseRepository, times(1)).getFavoriteForForecast(id);
        verify(api, times(1)).forecastById(id, units);
        verify(databaseRepository, times(1)).updateFavorite(any());

    }


}
