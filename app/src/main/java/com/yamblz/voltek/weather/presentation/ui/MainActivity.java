package com.yamblz.voltek.weather.presentation.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.presentation.base.BaseActivity;
import com.yamblz.voltek.weather.presentation.ui.about.AboutFragment;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastFragment;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Navigator {
    public static final String NAVIGATE_POSITION = "NAVIGATE_POSITION_ID";

    private Drawer navigation;

    @Nullable
    @BindView(R.id.drawer_content)
    FrameLayout navigationContainer;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean singlePane = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //check dual pane mode
        singlePane = navigationContainer==null;

        DrawerBuilder drawerBuilder = new DrawerBuilder().withActivity(this)
                .withHeaderHeight(DimenHolder.fromDp(240))
                .withTranslucentStatusBar(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    onDrawerItemClick(view.getId());
                    return true;
                })
                .withDrawerItems(customInflateMenu());


        if (!singlePane) {
            navigation = drawerBuilder.buildView();
            navigationContainer.addView(navigation.getSlider());
        } else {
            navigation = drawerBuilder.build();
        }


        if (savedInstanceState == null) {
            onDrawerItemClick(R.id.nav_forecast);
            if(singlePane) {
                navigation.setSelection(-1, false);
            }
        } else {
            if(!singlePane)
                navigation.setSelection(savedInstanceState.getInt(NAVIGATE_POSITION), false);
        }


    }


    /**
     * saving current select position
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(!singlePane)
            outState.putInt(NAVIGATE_POSITION, (int) navigation.getCurrentSelection());
    }


    /**
     * gets menu items from menu.xml
     *
     * @return
     */
    private List<IDrawerItem> customInflateMenu() {

        MenuInflater menuInflater = new SupportMenuInflater(this);
        MenuBuilder menu = new MenuBuilder(this);
        menuInflater.inflate(R.menu.activity_main_drawer, menu);
        List<IDrawerItem> result = new ArrayList<>();
        addMenuItems(result, menu);

        return result;
    }


    /**
     * saving current select position
     *
     * @param outState
     */


    /**
     * item click handling
     *
     * @param id - id of selected item
     */
    private void onDrawerItemClick(@IdRes int id) {

        Class fragmentClass = null;

        boolean isRoot = false;

        switch (id) {
            case R.id.nav_forecast:
                fragmentClass = ForecastFragment.class;
                isRoot = true;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;

                break;
            case R.id.nav_about:
                fragmentClass = AboutFragment.class;
                break;
        }

        //NPE, if will dummy menu item
        String tag = fragmentClass.getSimpleName();

        FragmentManager fm = getSupportFragmentManager();

        //if this item already selected
        if (fm.findFragmentByTag(tag) != null) {
            navigation.closeDrawer();
            return;
        }

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName());

        if (!singlePane || isRoot) {
            openAsRoot(fragment, tag);
        } else {
            openWithBackStack(fragment, tag);
        }



    }


    @Override
    public void onBackPressed() {
        if (singlePane && navigation.isDrawerOpen()) {
            navigation.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * convert items menu in IDrawerItem
     *
     * @param item
     * @param menu
     */
    private void addMenuItems(List<IDrawerItem> item, Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mMenuItem = menu.getItem(i);
            IDrawerItem iDrawerItem = new PrimaryDrawerItem()
                    .withName(mMenuItem.getTitle().toString())
                    .withIcon(mMenuItem.getIcon())
                    .withIdentifier(mMenuItem.getItemId())
                    .withEnabled(mMenuItem.isEnabled())
                    .withIconTintingEnabled(true)
                    .withSelectable(!singlePane)
                    .withSelectedIconColor(ContextCompat.getColor(getBaseContext(), R.color.material_drawer_dark_selected));
            item.add(iDrawerItem);
        }
    }


    @Override
    public void showNavigationDrawer() {
        navigation.openDrawer();
    }

    @Override
    public void setNavigationDrawerState(boolean enabled) {

        if (singlePane) {
            if (enabled) {
                navigation.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                navigation.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }

    @Override
    public void openNavigationDrawer() {
        if (singlePane)
            navigation.openDrawer();
    }

    @Override
    public void openWithBackStack(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment, tag)
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openAsRoot(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment, tag)
                .commit();
    }

    @Nullable
    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }


}
