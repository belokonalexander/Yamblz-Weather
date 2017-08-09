package com.yamblz.voltek.weather.presentation.ui.adapter.delegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.entity.WeatherUIModel;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.AdapterItem;
import com.yamblz.voltek.weather.presentation.ui.adapter.models.ForecastAdapterItem;
import com.yamblz.voltek.weather.utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 02.08.2017.
 */

public class ForecastAdapterDelegate extends AdapterDelegate<List<? extends AdapterItem>> {

    private final LayoutInflater inflater;

    public ForecastAdapterDelegate(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    protected boolean isForViewType(@NonNull List<? extends AdapterItem> items, int position) {
        return items.get(position) instanceof ForecastAdapterItem;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ForecastViewHolder(inflater.inflate(R.layout.item_forecast, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<? extends AdapterItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ((ForecastViewHolder) holder).bindTo((ForecastAdapterItem) items.get(position));
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.weather_desc)
        TextView weatherDesc;

        @BindView(R.id.time_text)
        TextView timeText;

        @BindView(R.id.temp_text)
        TextView temperatureText;

        @BindView(R.id.im_forecast)
        ImageView weatherImage;

        @BindView(R.id.item_wrapper)
        View wrapper;

        final SimpleDateFormat dateFormat;

        public ForecastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dateFormat = new SimpleDateFormat("HH:mm MM-dd", Locale.getDefault());
        }

        void bindTo(ForecastAdapterItem item) {

            WeatherUIModel forecast = item.getContent();
            temperatureText.setText(wrapper.getContext().getString(R.string.wthr_temperature, forecast.getTemperature()));
            String desc = forecast.getCondition().substring(0, 1).toUpperCase() + forecast.getCondition().substring(1);
            weatherDesc.setText(desc);
            weatherImage.setImageResource(WeatherUtils.getImageByCondition(forecast.getConditionId()));
            String time = dateFormat.format(forecast.getDate());
            timeText.setText(time);
        }
    }

}
