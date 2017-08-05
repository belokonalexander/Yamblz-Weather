package com.yamblz.voltek.weather;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * Created on 02.08.2017.
 */

public interface Navigator {

    void showNavigationDrawer();

    void setNavigationDrawerState(boolean enabled);

    void openNavigationDrawer();

    void openWithBackStack(Fragment fragment, String tag);

    void openAsRoot(Fragment fragment, String tag);

    Toolbar getCommonToolbar();

    void initToolbarNavigationView(Toolbar toolbar, Drawable toolbarNavigationIcon);
}
