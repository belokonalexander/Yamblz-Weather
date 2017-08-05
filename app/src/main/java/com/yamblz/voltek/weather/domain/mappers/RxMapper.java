package com.yamblz.voltek.weather.domain.mappers;

import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.functions.Function;

/**
 * Created on 05.08.2017.
 */

public class RxMapper {

    interface SingleItemMapper<T, S> {
        S getItem(T another);
    }

    private  <T, S> Function<List<T>, List<S>> mapList(SingleItemMapper<T, S> singleItemMapper) {
        return list -> {
            List<S> dest = new ArrayList<>();
            for (T sourceItem : list) {
                dest.add(singleItemMapper.getItem(sourceItem));
            }
            return dest;
        };
    }

    private  <T, S> Function<List<T>, Set<S>> mapSet(SingleItemMapper<T, S> singleItemMapper) {
        return list -> {
            Set<S> dest = new HashSet<S>();
            for (T sourceItem : list) {
                dest.add(singleItemMapper.getItem(sourceItem));
            }
            return dest;
        };
    }

    public Function<FavoriteCityModel, CityUIModel> favoriteCityModelToCityUIModel() {
        return favoriteCityModel -> new CityUIModel(favoriteCityModel.getCityId(), favoriteCityModel.getAlias());
    }

    public Function<List<FavoriteCityModel>, Set<CityUIModel>> favoriteCityModelListToCityUiModelSet() {
        return mapSet(another -> new CityUIModel(another.getCityId(), another.getAlias()));
    }

    public Function<WeatherResponseModel, WeatherUIModel> weatherResponseModelToWeatherUiModel() {
        return WeatherUIModel::new;
    }

    public Function<CityToIDModel, CityUIModel> cityToIDModelToCityUIModel() {
        return cityToIDModel -> new CityUIModel(cityToIDModel.getCityId(), cityToIDModel.getAlias());
    }

    public Function<WeatherResponseModel, CityToIDModel> weatherResponseModelToCityToIDModel() {
        return weatherResponseModel -> new CityToIDModel(weatherResponseModel.name, weatherResponseModel.id);
    }

    public Function<List<CityUIModel>, List<CityAdapterItem>> cityUIModelListToCityAdapterItemList() {
        return mapList(CityAdapterItem::new);
    }


}