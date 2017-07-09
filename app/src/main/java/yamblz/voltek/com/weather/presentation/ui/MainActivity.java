package yamblz.voltek.com.weather.presentation.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import yamblz.voltek.com.weather.R;
import yamblz.voltek.com.weather.presentation.base.BaseActivity;
import yamblz.voltek.com.weather.presentation.ui.about.AboutFragment;
import yamblz.voltek.com.weather.presentation.ui.forecast.ForecastFragment;
import yamblz.voltek.com.weather.presentation.ui.settings.SettingsFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_open, R.string.nav_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            replaceFragment(ForecastFragment.newInstance(), R.id.fragment_container);
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
                replaceFragment(ForecastFragment.newInstance(), R.id.fragment_container);
                break;
            case R.id.nav_settings:
                replaceFragment(SettingsFragment.newInstance(), R.id.fragment_container);
                break;
            case R.id.nav_about:
                replaceFragment(AboutFragment.newInstance(), R.id.fragment_container);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
