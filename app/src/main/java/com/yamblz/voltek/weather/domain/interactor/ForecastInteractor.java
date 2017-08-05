package com.yamblz.voltek.weather.domain.interactor;

import android.util.Pair;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;

import io.reactivex.Single;

public class ForecastInteractor {

    private WeatherAPI api;
    private StorageRepository storageRepository;
    private RxMapper rxMapper;


    public ForecastInteractor(WeatherAPI api, StorageRepository storageRepository, RxMapper rxMapper) {
        this.api = api;
        this.storageRepository = storageRepository;
        this.rxMapper = rxMapper;
    }

    public Single<WeatherUIModel> getCurrentWeather(boolean refresh) {

        Single<WeatherUIModel> apiRequest = storageRepository.getSelectedCity()
                .zipWith(storageRepository.getUnitsSettings(), Pair::new)
                .flatMap(pair -> api.byCityId(pair.first.id, pair.second))
                .zipWith(storageRepository.getSelectedCity(), (weatherResponseModel, cityUIModel) -> {
                    weatherResponseModel.name = cityUIModel.name;
                    return weatherResponseModel;
                }).map(rxMapper.weatherResponseModelToWeatherUiModel());

        if (refresh) {
            return apiRequest;
        } else {
            return /*storageRepository.getCurrent().onErrorResumeNext(*/apiRequest;
        }
    }


   /* private Function<WeatherResponseModel, WeatherUIModel> apiWeatherToUIWeather() {
        return WeatherUIModel::new;
    }*/


}
