package com.yamblz.voltek.weather.presentation.ui.forecast;

import android.support.annotation.Nullable;

import com.yamblz.voltek.weather.domain.Parameter;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.domain.interactor.CurrentWeatherInteractor;

public class ForecastPresenter {

    public interface View {

        void attachInputListeners();

        void render(Model model);
    }

    public class Model {

        @Nullable
        public Throwable error = null;
        @Nullable
        public WeatherUIModel data = null;

        public boolean isLoading = false;
    }

    private View view;
    private Model model;

    private CurrentWeatherInteractor interactor;

    public ForecastPresenter(View view, CurrentWeatherInteractor interactor) {
        attach(view);
        this.model = new Model();
        this.interactor = interactor;

        loadWeather(false);
    }

    // Lifecycle
    public void attach(View view) {
        this.view = view;
        view.attachInputListeners();
    }

    public void detach() {
        view = null;
    }

    // View callbacks
    public void notifyRefresh() {
        loadWeather(true);
    }

    // Private logic
    private void render() {
        if (view != null)
            view.render(model);
    }

    private void loadWeather(boolean refresh) {
        Parameter<Void> param = new Parameter<>();
        if (refresh)
            param.setFlag(CurrentWeatherInteractor.REFRESH);

        model.isLoading = true;
        model.data = null;
        model.error = null;
        render();

        interactor.execute(
                param,
                result -> {
                    model.data = result.getData();
                    render();
                },
                error -> {
                    model.isLoading = false;
                    model.error = error;
                    render();
                },
                () -> {
                    model.isLoading = false;
                    render();
                }
        );
    }
}
