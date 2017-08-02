package com.yamblz.voltek.weather.presentation.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Navigator {
    public static final String NAVIGATE_POSITION = "NAVIGATE_POSITION_ID";
    public static final String NAVIGATION_BACKPRESS = "NAVIGATION_BACKPRESS";

    private Drawer navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        navigation = new DrawerBuilder().withActivity(this)
                .withHeaderHeight(DimenHolder.fromDp(240))
                .withTranslucentStatusBar(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    onDrawerItemClick(view.getId());
                    return false;
                })
                .withDrawerItems(customInflateMenu())
                .build();


        if (savedInstanceState == null) {
            onDrawerItemClick(R.id.nav_forecast);
        } else {
            navigation.setSelection(savedInstanceState.getInt(NAVIGATE_POSITION), false);
        }

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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAVIGATE_POSITION, (int) navigation.getCurrentSelection());
    }


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
            return;
        }

        Fragment fragment = Fragment.instantiate(this, fragmentClass.getName());

        if (isRoot) {
            fm.beginTransaction()
                    .replace(R.id.main_content, fragment, tag)
                    .commit();
        } else
            openInThisContainer(fragment);


        navigation.setSelection(id, false);

    }


    @Override
    public void onBackPressed() {
        if (navigation.isDrawerOpen()) {
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
        if (enabled) {
            navigation.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            navigation.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void openNavigationDrawer() {
        navigation.openDrawer();
    }

    @Override
    public void openInThisContainer(Fragment fragment) {
        Bundle additional = fragment.getArguments();
        if (additional == null)
            additional = new Bundle();

        additional.putBoolean(Navigator.NAVIGATION_BACKPRESS, true);

        fragment.setArguments(additional);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment, null)
                //.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_right)
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }
}
