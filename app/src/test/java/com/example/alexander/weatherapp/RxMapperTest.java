package com.example.alexander.weatherapp;

import com.example.alexander.weatherapp.helper.ApiDataHelper;
import com.google.gson.JsonPrimitive;
import com.yamblz.voltek.weather.data.api.weather.models.WeatherResponseModel;
import com.yamblz.voltek.weather.data.api.weather.models.forecast.ForecastResponseModel;
import com.yamblz.voltek.weather.data.database.models.CityToIDModel;
import com.yamblz.voltek.weather.data.database.models.FavoriteCityModel;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.mappers.RxMapper;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.CityAdapterItem;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;

/**
 * Created on 09.08.2017.
 */

public class RxMapperTest {

    private RxMapper rxMapper = new RxMapper();

    @Test
    public void favoriteCityModelListToCityUiModelTreeSetTest() throws InterruptedException {

        List<FavoriteCityModel> array = new ArrayList<>();

        String prfx = "item";
        int to = 10;

        CityUIModel[] expected = new CityUIModel[to];

        for (int j = 0; j < 2; j++)
            for (int i = 0; i < to; i++) {
                array.add(new FavoriteCityModel(prfx + i, i));
                if (j == 0)
                    expected[i] = new CityUIModel(i, prfx + i);
            }

        TestObserver<CityUIModel> testObserver = TestObserver.create();
        Observable.just(array).map(rxMapper.favoriteCityModelListToCityUiModelTreeSet()).flatMap(new Function<Set<CityUIModel>, ObservableSource<CityUIModel>>() {
            @Override
            public ObservableSource<CityUIModel> apply(@NonNull Set<CityUIModel> cityUIModels) throws Exception {
                return Observable.fromIterable(cityUIModels);
            }
        }).subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValues(expected);
        testObserver.assertValueCount(to);

    }

    @Test
    public void favoriteCityModelToCityUIModelTest() throws InterruptedException {

        String alias = "item";
        int id = 100;

        FavoriteCityModel from = new FavoriteCityModel(alias, id);
        CityUIModel expected = new CityUIModel(id, alias);

        TestObserver<CityUIModel> testObserver = TestObserver.create();

        Single.just(from).map(rxMapper.favoriteCityModelToCityUIModel())
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValueCount(1);
        testObserver.assertValue(expected);

    }

    @Test
    public void cityToIDModelToCityUIModelTest() throws InterruptedException {

        String alias = "item";
        int id = 100;

        CityToIDModel from = new CityToIDModel(alias, id);
        CityUIModel expected = new CityUIModel(id, alias);

        TestObserver<CityUIModel> testObserver = TestObserver.create();

        Single.just(from).map(rxMapper.cityToIDModelToCityUIModel())
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValueCount(1);
        testObserver.assertValue(expected);

    }

    @Test
    public void weatherResponseModelToCityToIDModel() throws InterruptedException {

        String alias = "item";
        int id = 100;

        WeatherResponseModel from = ApiDataHelper.getModel(WeatherResponseModel.class, (ApiDataHelper.Modify) jsonObject -> {
            jsonObject.add("id", new JsonPrimitive(id));
            jsonObject.add("name", new JsonPrimitive(alias));
        });

        CityToIDModel expected = new CityToIDModel(alias, id);

        TestObserver<CityToIDModel> testObserver = TestObserver.create();

        Single.just(from).map(rxMapper.weatherResponseModelToCityToIDModel())
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValueCount(1);
        testObserver.assertValue(expected);

    }

    @Test
    public void cityUIModelListToCityAdapterItemListTest() throws InterruptedException {

        String alias = "item";
        int to = 10;

        List<CityUIModel> from = new ArrayList<>();
        CityAdapterItem[] expected = new CityAdapterItem[to];

        for(int i =0; i < to; i++) {
            CityUIModel cityUIModel = new CityUIModel(i, alias + i);
            from.add(cityUIModel);
            expected[i] = new CityAdapterItem(cityUIModel);
        }

        TestObserver<CityAdapterItem> testObserver = TestObserver.create();

        Observable.just(from).map(rxMapper.cityUIModelListToCityAdapterItemList())
                .flatMap(new Function<List<CityAdapterItem>, ObservableSource<CityAdapterItem>>() {
                    @Override
                    public ObservableSource<CityAdapterItem> apply(@NonNull List<CityAdapterItem> cityAdapterItems) throws Exception {
                        return Observable.fromIterable(cityAdapterItems);
                    }
                })
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValueCount(to);
        testObserver.assertValues(expected);

    }

    @Test
    public void forecastResponseModelToWeatherUIModelListTest() throws InterruptedException {

        ForecastResponseModel from = ApiDataHelper.getModel(ForecastResponseModel.class);

        int count = from.weather.size();

        TestObserver<WeatherUIModel> testObserver = TestObserver.create();
        WeatherUIModel[] expected = new WeatherUIModel[count];
        for(int i =0; i < count; i++)
            expected[i] = new WeatherUIModel(from.weather.get(i), from.city.name);

        Observable.just(from).map(rxMapper.forecastResponseModelToWeatherUIModelList())
                .flatMap(new Function<List<WeatherUIModel>, ObservableSource<WeatherUIModel>>() {
                    @Override
                    public ObservableSource<WeatherUIModel> apply(@NonNull List<WeatherUIModel> weatherUIModels) throws Exception {
                        return Observable.fromIterable(weatherUIModels);
                    }
                })
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValueCount(count);
        testObserver.assertValues(expected);

    }

    @Test
    public void cityUIModelToSimpleNameTest() throws InterruptedException {

        String alias = "item";
        int id = 100;

        CityUIModel from = new CityUIModel(id, alias);

        TestObserver<String> testObserver = TestObserver.create();

        Single.just(from).map(rxMapper.cityUIModelToSimpleName())
                .subscribe(testObserver);

        testObserver.await();
        testObserver.assertComplete();
        testObserver.assertValueCount(1);
        testObserver.assertValue(alias);

    }

}
