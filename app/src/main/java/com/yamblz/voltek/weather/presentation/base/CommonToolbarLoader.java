package com.yamblz.voltek.weather.presentation.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;

/**
 * Created on 03.08.2017.
 */

public class CommonToolbarLoader implements ToolbarLoader {

    private final Navigator navigationManager;
    private Toolbar toolbar;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final ToolbarNavigationListener toolbarNavigationListener;


    public CommonToolbarLoader(Navigator navigator, FragmentManager fragmentManager, Context context, Toolbar toolbar, ToolbarNavigationListener listener) {
        this.navigationManager = navigator;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.toolbar = toolbar;
        this.toolbarNavigationListener = listener;
    }

    @Override
    public void initToolbar(String title) {
        boolean globalToolbar = false;
        //has no own toolbar - get activity toolbar
        if (toolbar == null) {
            globalToolbar = true;
            toolbar = navigationManager.getCommonToolbar();
        }

        toolbar.setTitle(title);


        int backstackCount = fragmentManager.getBackStackEntryCount();
        boolean isRoot = backstackCount == 0;
        Drawable toolbarNavigationIcon;
        if (!isRoot) {
            navigationManager.setNavigationDrawerState(false);
            toolbarNavigationIcon = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_arrow_back_black_24dp, null);
            toolbar.setNavigationOnClickListener(v -> {
                if (toolbarNavigationListener != null) {
                    toolbarNavigationListener.onBackClick();
                }
            });
        } else {
            toolbarNavigationIcon = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_menu_black_24dp, null);
            navigationManager.setNavigationDrawerState(true);
            toolbar.setNavigationOnClickListener(v -> {
                if (toolbarNavigationListener != null) {
                    toolbarNavigationListener.onMenuClick();
                }
            });
        }

        navigationManager.initToolbarNavigationView(toolbar, toolbarNavigationIcon);
    }


    public interface ToolbarNavigationListener {

        void onBackClick();

        void onMenuClick();

    }

}
