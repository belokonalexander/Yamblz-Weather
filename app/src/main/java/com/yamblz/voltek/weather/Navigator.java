package com.yamblz.voltek.weather;

import android.support.v4.app.Fragment;

/**
 * Created on 02.08.2017.
 */

public interface Navigator {

    void showNavigationDrawer();

    void setNavigationDrawerState(boolean enabled);

    void openNavigationDrawer();

    String NAVIGATION_BACKPRESS = "NAVIGATION_BACKPRESS";

    void openInThisContainer(Fragment whichWillOpen);
}
