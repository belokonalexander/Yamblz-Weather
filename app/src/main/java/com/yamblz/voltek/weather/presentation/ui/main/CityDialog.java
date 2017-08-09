package com.yamblz.voltek.weather.presentation.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseDialogFragment;

/**
 * Created on 04.08.2017.
 */

public class CityDialog extends BaseDialogFragment {

    private CityUIModel model;

    public void initData(CityUIModel model, NoticeDialogListener listener) {
        this.model = model;
        this.positiveListener = listener;
    }

    public CityDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_city)
                .setTitle(model.name)
                .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    if (positiveListener != null)
                        positiveListener.onDialogPositiveClick(CityDialog.this);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> {

                });

        return builder.create();
    }
}
