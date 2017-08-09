package com.example.alexander.weatherapp.Helpers;


import io.reactivex.observers.TestObserver;

public class InteractorTestHelperObserver<T> {

    private final TestObserver<Result<T>> listener;

    public InteractorTestHelperObserver(TestObserver<Result<T>> listener) {
        this.listener = listener;
    }

    public void onNext(Result<T> result) {
        listener.onNext(result);
    }

    public void onThrowable(Throwable t) {
        listener.onError(t);
    }

    public void onComplete() {
        listener.onComplete();
    }

    public TestObserver<Result<T>> getListener() {
        return listener;
    }

}
