package com.yamblz.voltek.weather.data.api.weather;

import android.content.Context;

import com.yamblz.voltek.weather.BuildConfig;
import com.yamblz.voltek.weather.data.Provider;
import com.yamblz.voltek.weather.data.api.ApiUtils;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.exception.NoConnectionException;
import com.yamblz.voltek.weather.domain.exception.RequestFailedException;

import java.io.IOException;

import retrofit2.Response;

public class WeatherAPIDelegate implements Provider.API.Weather {

    private Context context;
    private WeatherAPI api;

    public WeatherAPIDelegate(Context context, WeatherAPI api) {
        this.context = context;
        this.api = api;
    }

    @Override
    public WeatherUIModel getCurrent() throws NoConnectionException, RequestFailedException {
        if (!ApiUtils.isConnected(context))
            throw new NoConnectionException();

        try {
            Response<WeatherResponseModel> call = api.byCityName(BuildConfig.ApiKey, "Moscow").execute();

            if (call.isSuccessful())
                return new WeatherUIModel(call.body());
            else
                throw new RequestFailedException();
        } catch (IOException e) {
            throw new RequestFailedException();
        }
    }
}
