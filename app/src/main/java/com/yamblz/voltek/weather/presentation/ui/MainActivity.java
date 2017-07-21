package com.yamblz.voltek.weather.presentation.ui;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.presentation.base.BaseActivity;
import com.yamblz.voltek.weather.presentation.ui.about.AboutFragment;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastFragment;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @IdRes
    private static final int FRAGMENT_CONTAINER = R.id.fragment_container;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            replaceFragment(ForecastFragment.newInstance(), FRAGMENT_CONTAINER);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_forecast:
                replaceFragment(ForecastFragment.newInstance(), FRAGMENT_CONTAINER);
                break;
            case R.id.nav_settings:
                replaceFragment(SettingsFragment.newInstance(), FRAGMENT_CONTAINER);
                break;
            case R.id.nav_about:
                replaceFragment(AboutFragment.newInstance(), FRAGMENT_CONTAINER);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
