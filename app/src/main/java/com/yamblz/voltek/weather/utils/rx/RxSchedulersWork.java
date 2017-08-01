package com.yamblz.voltek.weather.utils.rx;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 31.07.2017.
 */

public class RxSchedulersWork extends RxSchedulers {
    @Override
    public Scheduler getMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getIOScheduler() {
        return Schedulers.io();
    }

    @Override
    public Scheduler getComputationScheduler() {
        return Schedulers.computation();
    }
}
