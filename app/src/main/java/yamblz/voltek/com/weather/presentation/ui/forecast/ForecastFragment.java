package yamblz.voltek.com.weather.presentation.ui.forecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yamblz.voltek.com.weather.R;
import yamblz.voltek.com.weather.presentation.base.BaseFragment;

public class ForecastFragment extends BaseFragment {

    public static ForecastFragment newInstance() {

        Bundle args = new Bundle();

        ForecastFragment fragment = new ForecastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.title_forecast);
    }
}
