package com.example.alexander.weatherapp.Helpers;


import io.reactivex.subscribers.TestSubscriber;

public class InteractorTestHelper<T> {

    private final TestSubscriber<Result<T>> listener;

    public InteractorTestHelper(TestSubscriber<Result<T>> listener) {
        this.listener = listener;
    }

    public void onNext(Result<T> result){
        listener.onNext(result);
    }

    public void onThrowable(Throwable t){
        listener.onError(t);
    }

    public void onComplete(){
        listener.onComplete();
    }

    public TestSubscriber<Result<T>> getListener() {
        return listener;
    }

}
