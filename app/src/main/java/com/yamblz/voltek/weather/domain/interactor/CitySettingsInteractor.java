package com.yamblz.voltek.weather.domain.interactor;

import android.support.annotation.NonNull;

import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.database.DatabaseRepository;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

/**
 * Created on 31.07.2017.
 */

public class CitySettingsInteractor {

    private final WeatherAPI api;
    private final DatabaseRepository databaseRepository;
    private final RxMapper rxMapper;

    public CitySettingsInteractor(WeatherAPI api, DatabaseRepository databaseRepository, RxMapper rxMapper) {
        this.api = api;
        this.databaseRepository = databaseRepository;
        this.rxMapper = rxMapper;
    }


    public Single<List<CityAdapterItem>> getCitySuggestions(@NonNull String prefix) {
        return databaseRepository.getCityByPrefix(prefix)
                .flatMap(toCity())
                .map(rxMapper.cityUIModelListToCityAdapterItemList());
    }

    public Single<CityUIModel> saveCity(@NonNull CityUIModel city) {

        if (city.id > 0) {
            return getCorrectSaveCity(city);
        } else {

            return databaseRepository.getCityByName(city.name)
                    .onErrorResumeNext(throwable -> api.byCityName(city.name, null)
                            .map(rxMapper.weatherResponseModelToCityToIDModel())
                            .flatMap(cityToIDModel -> databaseRepository.saveCity(cityToIDModel).toSingleDefault(cityToIDModel)))
                    .map(rxMapper.cityToIDModelToCityUIModel())
                    .flatMap(this::getCorrectSaveCity);


        }
    }

    private Single<CityUIModel> getCorrectSaveCity(@NonNull CityUIModel city) {
        return databaseRepository.saveAsFavoriteIfNotExists(new CityToIDModel(city.name, city.id), true)
                .toSingleDefault(city);
    }


    private Function<List<CityToIDModel>, SingleSource<List<CityUIModel>>> toCity() {
        return cityToIDModels -> Single.fromCallable(() -> {
            List<CityUIModel> list = new ArrayList<>();
            for (CityToIDModel model : cityToIDModels) {
                list.add(new CityUIModel(model.getCityId(), model.getAlias()));
            }
            return list;
        });
    }


}
