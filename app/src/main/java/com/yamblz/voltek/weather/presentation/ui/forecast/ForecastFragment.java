package com.yamblz.voltek.weather.presentation.ui.forecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yamblz.voltek.weather.Injector;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;
import com.yamblz.voltek.weather.utils.StringUtils;
import com.yamblz.voltek.weather.utils.WeatherUtils;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ForecastFragment extends BaseFragment implements ForecastPresenter.View {

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.ll_content)
    LinearLayout contentContainer;

    @BindView(R.id.tv_empty_state)
    TextView emptyStateTv;

    @BindView(R.id.im_weather_icon)
    ImageView weatherIconIm;
    @BindView(R.id.tv_description)
    TextView descriptionTv;
    @BindView(R.id.tv_temperature)
    TextView temperatureTv;
    @BindView(R.id.tv_humidity)
    TextView humidityTv;

    private ForecastPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.title_forecast);

        presenter = Injector.attachForecastPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Injector.detachForecastPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getActivity().isFinishing()) {
            Injector.destroyForecastPresenter();
        }
    }

    @Override
    public void attachInputListeners() {
        swipeContainer.setOnRefreshListener(() -> presenter.notifyRefresh());
    }

    @Override
    public void render(ForecastPresenter.Model model) {
        swipeContainer.setRefreshing(model.isLoading);

        if (model.error != null) {
            contentContainer.setVisibility(GONE);

            emptyStateTv.setText(getString(StringUtils.fromError(model.error)));
            emptyStateTv.setVisibility(VISIBLE);
        } else if (model.data != null) {
            weatherIconIm.setImageResource(
                    WeatherUtils.getImageByCondition(model.data.getConditionId()));
            descriptionTv.setText(model.data.getCondition());
            temperatureTv.setText(getString(R.string.wthr_temperature, model.data.getTemperature()));
            humidityTv.setText(getString(R.string.wthr_humidity, model.data.getHumidity()));

            emptyStateTv.setVisibility(GONE);
            contentContainer.setVisibility(VISIBLE);
        }
    }
}
