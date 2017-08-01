package com.yamblz.voltek.weather.domain.interactor;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created on 31.07.2017.
 */

public class SettingsInteractor {

    private WeatherAPI api;
    private StorageRepository storage;
    private DatabaseRepository databaseRepository;

    public SettingsInteractor(WeatherAPI api, StorageRepository storageRepository, DatabaseRepository databaseRepository) {
        this.api = api;
        this.storage = storageRepository;
        this.databaseRepository = databaseRepository;
    }

    public Single<CityUIModel> getCurrentCity() {
        return storage.getSelectedCity();
    }

    public Single<List<CityUIModel>> getCitySuggestions(String prefix) {
        return databaseRepository.getCityByPrefix(prefix).flatMap(toCity());
    }

    public Single<CityUIModel> saveCity(CityUIModel city) {
        if (city == null)
            return Single.error(new IllegalArgumentException());

        if (city.id > 0) {
            return getCorrectSaveCity(city);
        } else {

            return databaseRepository.getCityByName(city.name)
                    .onErrorResumeNext(throwable -> api.byCityName(city.name)
                            .map(apiWeatherToDBCityMapper())
                            .flatMap(cityToIDModel -> databaseRepository.saveCity(cityToIDModel).toSingleDefault(cityToIDModel)))
                    .map(dbCityToUiCityMapper())
                    .flatMap(this::getCorrectSaveCity);


        }
    }

    private Single<CityUIModel> getCorrectSaveCity(CityUIModel city) {
        return storage.putSelectedCity(city).toSingleDefault(city);
    }

    private Function<CityToIDModel, CityUIModel> dbCityToUiCityMapper() {
        return cityToIDModel -> new CityUIModel(cityToIDModel.getCityId(), cityToIDModel.getAlias());
    }


    private Function<WeatherResponseModel, CityToIDModel> apiWeatherToDBCityMapper() {
        return weatherResponseModel -> new CityToIDModel(weatherResponseModel.name, weatherResponseModel.id);
    }

    private Function<Collection<CityToIDModel>, SingleSource<List<CityUIModel>>> toCity() {
        return cityToIDModels -> Single.fromCallable(() -> {
            List<CityUIModel> list = new ArrayList<>();
            for (CityToIDModel model : cityToIDModels) {
                list.add(new CityUIModel(model.getCityId(), model.getAlias()));
            }
            return list;
        });
    }

}
