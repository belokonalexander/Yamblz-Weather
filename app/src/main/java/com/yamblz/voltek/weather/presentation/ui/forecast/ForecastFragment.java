package com.yamblz.voltek.weather.presentation.ui.forecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yamblz.voltek.weather.Injector;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;

import butterknife.BindView;

public class ForecastFragment extends BaseFragment implements ForecastPresenter.View {

    public static ForecastFragment newInstance() {
        return new ForecastFragment();
    }

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

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
    }
}
