package com.yamblz.voltek.weather.data.platform.jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.yamblz.voltek.weather.data.api.weather.WeatherAPI;
import com.yamblz.voltek.weather.data.api.weather.response.WeatherResponseModel;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;


public class WeatherJob extends Job {

    static final String TAG = "GET_WEATHER_JOB";


    private WeatherAPI weatherApi;
    //private WeatherModelToCityWeatherMapper mapper;
    private StorageRepository storageRepository;

    WeatherJob(WeatherAPI weatherApi, StorageRepository storageRepository) {

        this.weatherApi = weatherApi;
        //this.mapper = mapper;
        this.storageRepository = storageRepository;
    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        final boolean[] flag = {false};

        storageRepository.getSelectedCity()
                .flatMap(new Function<CityUIModel, SingleSource<WeatherResponseModel>>() {
                    @Override
                    public SingleSource<WeatherResponseModel> apply(@NonNull CityUIModel cityUIModel) throws Exception {
                        return weatherApi.byCityId(cityUIModel.id);
                    }
                })
                .map(WeatherUIModel::new)
                .flatMap(new Function<WeatherUIModel, SingleSource<WeatherUIModel>>() {
                    @Override
                    public SingleSource<WeatherUIModel> apply(@NonNull WeatherUIModel weatherUIModel) throws Exception {
                        return storageRepository.putCurrent(weatherUIModel).toSingleDefault(weatherUIModel);
                    }})
                .subscribe(new DisposableSingleObserver<WeatherUIModel>() {
                    @Override
                    public void onSuccess(@NonNull WeatherUIModel weatherUIModel) {
                        flag[0] = true;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        flag[0] = false;
                    }
                });


        return flag[0] ? Result.SUCCESS : Result.FAILURE;
    }

    static void scheduleJob(int minutes) {

        new JobRequest.Builder(WeatherJob.TAG)
                //.setRequiresCharging(true)
                .setPersisted(true)                                          //задача невоспреимчива к перезагрузке устройства
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)   //задача выполняется при наличии интернет соединения
                .setUpdateCurrent(true)                                     //переписываю задачу с тем же тэгом
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(14))  //чтобы выполнение задачи было отложенным
                .build()
                .schedule();
    }
}
