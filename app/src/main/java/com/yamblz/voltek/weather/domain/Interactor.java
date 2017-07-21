package com.yamblz.voltek.weather.domain;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public abstract class Interactor<ParameterType, ResultType> {

    private final CompositeDisposable subscription = new CompositeDisposable();

    private final Scheduler jobScheduler;
    private final Scheduler uiScheduler;

    public Interactor(Scheduler jobScheduler, Scheduler uiScheduler) {
        this.jobScheduler = jobScheduler;
        this.uiScheduler = uiScheduler;
    }

    protected abstract Observable<Result<ResultType>> build(Parameter<ParameterType> parameter);

    public void execute(
            Parameter<ParameterType> parameter,
            Consumer<Result<ResultType>> onNext,
            Consumer<Throwable> onError,
            Action onComplete
    ) {
        subscription.add(
                build(parameter)
                        .subscribeOn(jobScheduler)
                        .observeOn(uiScheduler)
                        .subscribe(onNext, onError, onComplete)
        );
    }

    public void unsubscribe() {
        subscription.clear();
    }
}
