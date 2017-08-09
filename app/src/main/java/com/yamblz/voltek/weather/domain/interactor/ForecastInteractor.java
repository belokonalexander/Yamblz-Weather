package com.yamblz.voltek.weather.domain.interactor;

import android.util.Pair;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ForecastInteractor {

    private final WeatherAPI api;
    private final StorageRepository storageRepository;
    private final DatabaseRepository databaseRepository;
    private final RxMapper rxMapper;


    public ForecastInteractor(WeatherAPI api, StorageRepository storageRepository, DatabaseRepository databaseRepository, RxMapper rxMapper) {
        this.api = api;
        this.storageRepository = storageRepository;
        this.databaseRepository = databaseRepository;
        this.rxMapper = rxMapper;
    }


    public Single<String> getSelectedCityName() {
        return storageRepository.getSelectedCity().map(rxMapper.cityUIModelToSimpleName());
    }

    public Observable<List<WeatherUIModel>> getCurrentWeather(boolean refresh) {

        if (refresh) {
            return getApiRequest().toObservable();
        } else {
            //при тестировании обнаружил, что concat прерывался во время ошибки dbRequest и переходил в терминальное состояние onError, вместо запроса к api
            //хотя в observeOn был выставлен флаг delayError, пришлось выкручиваться так :/
            return Observable.concat(getDbRequest().toObservable().onErrorResumeNext(throwable -> {
                return Observable.empty();
            }), getApiRequest().toObservable());

        }
    }

    private Single<Pair<CityUIModel, String>> getPreset() {
        return storageRepository.getSelectedCity()
                .zipWith(storageRepository.getUnitsSettings(), (cityUIModel, s) -> {
                    databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(cityUIModel.name, cityUIModel.id), false).subscribe();
                    return new Pair<>(cityUIModel, s);
                });
    }

    private Single<List<WeatherUIModel>> getApiRequest() {
        return getPreset().flatMap(pair -> api.forecastById(pair.first.id, pair.second))
                .zipWith(storageRepository.getSelectedCity(), (forecastResponseModel, cityUIModel) -> {
                    forecastResponseModel.city.name = cityUIModel.name;
                    return forecastResponseModel;
                }).doOnSuccess(forecastResponseModel -> databaseRepository.updateFavorite(forecastResponseModel).subscribe())
                .map(rxMapper.forecastResponseModelToWeatherUIModelList());
    }

    private Single<List<WeatherUIModel>> getDbRequest() {
        return getPreset().flatMap(pair -> databaseRepository.getFavoriteForForecast(pair.first.id))
                .zipWith(storageRepository.getSelectedCity(), (favoriteCityModel, cityUIModel) -> {
                    favoriteCityModel.city.name = cityUIModel.name;
                    return favoriteCityModel;
                }).map(rxMapper.forecastResponseModelToWeatherUIModelList());
    }
}
