package com.yamblz.voltek.weather.presentation.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;

/**
 * Created on 03.08.2017.
 */

public interface FragmentContent {

    @LayoutRes
    int getLayout();

    @StringRes
    int getTitle();

}
