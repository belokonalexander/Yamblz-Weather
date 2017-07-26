package com.yamblz.voltek.weather.data.api.weather;

import android.content.Context;

import com.yamblz.voltek.weather.BuildConfig;
import com.yamblz.voltek.weather.data.DataProvider;
import com.yamblz.voltek.weather.data.api.ApiConst;
import com.yamblz.voltek.weather.data.api.ApiUtils;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Response;


// FIXME: 26.07.2017 too much boilerplate :( (Alexander)
public class WeatherAPIDelegate implements DataProvider.API.Weather {

    private Context context;
    private WeatherAPI api;

    public WeatherAPIDelegate(Context context, WeatherAPI api) {
        this.context = context;
        this.api = api;
    }

    @Override
    public WeatherUIModel getCurrent(String cityName) throws NoConnectionException, RequestFailedException {
        if (!ApiUtils.isConnected(context))
            throw new NoConnectionException();

        try {
            Response<WeatherResponseModel> call = api.byCityName(
                    BuildConfig.ApiKey,
                    Locale.getDefault().toString().substring(0, 2),
                    ApiConst.UNITS_METRIC,
                    cityName
            ).execute();

            if (call.isSuccessful())
                return new WeatherUIModel(call.body());
            else
                throw new RequestFailedException();
        } catch (IOException e) {
            throw new RequestFailedException();
        }
    }

    public WeatherUIModel getCurrentByID(int id) throws NoConnectionException, RequestFailedException {
        if (!ApiUtils.isConnected(context))
            throw new NoConnectionException();

        try {
            Response<WeatherResponseModel> call = api.byCityId(
                    BuildConfig.ApiKey,
                    Locale.getDefault().toString().substring(0, 2),
                    ApiConst.UNITS_METRIC,
                    id
            ).execute();


            if (call.isSuccessful()) {
                return new WeatherUIModel(call.body());
            } else {
                throw new RequestFailedException();
            }
        } catch (IOException e) {
            throw new RequestFailedException();
        }
    }

    @Override
    public CityUIModel getCity(String cityName) throws NoConnectionException, RequestFailedException {
        if (!ApiUtils.isConnected(context))
            throw new NoConnectionException();

        try {
            Response<WeatherResponseModel> call = api.byCityName(
                    BuildConfig.ApiKey,
                    Locale.getDefault().toString().substring(0, 2),
                    ApiConst.UNITS_METRIC,
                    cityName
            ).execute();

            if (call.isSuccessful())
                return new CityUIModel(call.body());
            else
                throw new RequestFailedException();
        } catch (IOException e) {
            throw new RequestFailedException();
        }
    }
}
