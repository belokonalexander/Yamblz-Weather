package com.yamblz.voltek.weather.utils.rx;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 10.08.2017.
 */

public class RxSchedulersTest extends RxSchedulers {
    @Override
    protected Scheduler getMainThreadScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    protected Scheduler getIOScheduler() {
        return Schedulers.trampoline();
    }

    @Override
    protected Scheduler getComputationScheduler() {
        return Schedulers.trampoline();
    }
}
