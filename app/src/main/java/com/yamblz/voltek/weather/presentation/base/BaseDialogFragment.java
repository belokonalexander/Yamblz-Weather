package com.yamblz.voltek.weather.presentation.base;


import android.support.v4.app.DialogFragment;

/**
 * Created on 04.08.2017.
 */

public class BaseDialogFragment extends DialogFragment {

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    protected NoticeDialogListener positiveListener;

    public BaseDialogFragment() {
    }
}
