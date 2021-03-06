package com.yamblz.voltek.weather.di.modules;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.Gson;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.data.api.NetworkUtils;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created on 31.07.2017.
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    WeatherAPI provideWeatherApi(NetworkUtils utils, Context context, Gson gson) {

        String idAlias = context.getString(R.string.weather_api_id_alias);
        String weatherKey = context.getString(R.string.weather_api_key);
        String countAlias = context.getString(R.string.weather_api_count_alias);
        String count = context.getString(R.string.weather_api_default_count);
        String langAlias = context.getString(R.string.weather_api_lang_alias);
        String selectedLang = context.getString(R.string.weather_api_lang);

        List<Pair<String, String>> params = Arrays.asList(new Pair<>(idAlias, weatherKey),
                new Pair<>(countAlias, count),
                new Pair<>(langAlias, selectedLang));

        return getService(WeatherAPI.class, utils, context.getString(R.string.base_weather_url), gson, params);
    }

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils(Context context) {
        return new NetworkUtils(context);
    }


    private <T> T getService(@NonNull Class<T> service, @NonNull NetworkUtils utils, @NonNull String baseUrl, Gson gson,
                             @NonNull List<Pair<String, String>> constantParams) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getHttpClient(utils, constantParams))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(service);
    }


    private OkHttpClient getHttpClient(NetworkUtils networkUtils, List<Pair<String, String>> constantParams) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            if (!constantParams.isEmpty()) {

                HttpUrl.Builder builder = originalHttpUrl.newBuilder();
                for (Pair<String, String> pair : constantParams) {
                    builder.addQueryParameter(pair.first, pair.second);
                }
                originalHttpUrl = builder.build();
            }

            Request.Builder requestBuilder = original.newBuilder()
                    .url(originalHttpUrl);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });


        return httpClient.addInterceptor(getNetworkConnectInterceptor(networkUtils)).build();
    }


    private Interceptor getNetworkConnectInterceptor(NetworkUtils utils) {
        return chain -> {

            Request request = chain.request();

            if (!utils.isNetworkAvailable()) {
                //todo for caching maybe
                throw new UnknownHostException("Unable to resolve host \"" + request.url().host() + "\"");
            }

            //run real api query
            Response response = null;
            response = chain.proceed(request);

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                throw new NullPointerException();
            }

            String responseBodyString = responseBody.string();

            //maybe caching
            if (response.code() == 200) {
                //TODO
            }

            return response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();
        };
    }

}
