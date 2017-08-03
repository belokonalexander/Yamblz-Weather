package com.yamblz.voltek.weather.presentation.ui;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.DimenHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.presentation.base.BaseActivity;
import com.yamblz.voltek.weather.presentation.ui.about.AboutFragment;
import com.yamblz.voltek.weather.presentation.ui.favorites.FavoritesPresenter;
import com.yamblz.voltek.weather.presentation.ui.favorites.FavoritesView;
import com.yamblz.voltek.weather.presentation.ui.forecast.ForecastFragment;
import com.yamblz.voltek.weather.presentation.ui.menu.items.MainDrawerItem;
import com.yamblz.voltek.weather.presentation.ui.menu.items.WeatherItem;
import com.yamblz.voltek.weather.presentation.ui.settings.SelectCity.SelectCityFragment;
import com.yamblz.voltek.weather.presentation.ui.settings.SettingsFragment;
import com.yamblz.voltek.weather.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Navigator, FavoritesView {
    public static final String NAVIGATE_POSITION = "NAVIGATE_POSITION_ID";

    @InjectPresenter
    FavoritesPresenter favoritesPresenter;

    @ProvidePresenter
    FavoritesPresenter provideFavoritesPresenter() {
        return WeatherApp.get(this).getAppComponent().getFavoritesPresenter();
    }

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
        singlePane = navigationContainer == null;

        DrawerBuilder drawerBuilder = new DrawerBuilder().withActivity(this)
                .withHeader(R.layout.drawer_header_layout)
                .withHeaderHeight(DimenHolder.fromDp(getResources().getDimensionPixelSize(R.dimen.drawer_header_height)))
                .withScrollToTopAfterClick(false)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    onDrawerItemClick(drawerItem);
                    return true;
                });


        drawerBuilder.withStickyDrawerItems(customInflateMenu(R.menu.sticky_footer_drawer));
        drawerBuilder.withDrawerItems(customInflateMenu(R.menu.after_items_menu));

        if (!singlePane) {
            navigation = drawerBuilder.buildView();
            navigationContainer.addView(navigation.getSlider());
        } else {
            navigation = drawerBuilder.build();
        }


    }


    /**
     * gets menu items from menu.xml
     *
     * @return
     */
    private List<IDrawerItem> customInflateMenu(@MenuRes int menuId) {

        MenuInflater menuInflater = new SupportMenuInflater(this);
        MenuBuilder menu = new MenuBuilder(this);
        menuInflater.inflate(menuId, menu);
        List<IDrawerItem> result = new ArrayList<>();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            PrimaryDrawerItem drawerItem;
            drawerItem = new MainDrawerItem(menuItem.getItemId());
            result.add(initiateItemField(drawerItem, menuItem.getTitle().toString(), menuItem.getIcon()));
        }

        return result;
    }

    private IDrawerItem initiateItemField(PrimaryDrawerItem drawerItem, String title, Drawable icon) {

        IDrawerItem item = drawerItem.withName(title)

                .withIcon(icon)
                .withIconTintingEnabled(true)
                .withSelectable(!singlePane)
                .withSelectedIconColor(ContextCompat.getColor(getBaseContext(), R.color.material_drawer_dark_selected));

        if (drawerItem instanceof MainDrawerItem) {
            item.withIdentifier(((MainDrawerItem) drawerItem).getItemId());
        }

        return item;
    }


    private void onDrawerItemClick(IDrawerItem drawerItem) {

        Class fragmentClass = null;
        boolean isRoot = false;

        //individual item of menu
        if (drawerItem instanceof MainDrawerItem) {
            int id = ((MainDrawerItem) drawerItem).getItemId();
            switch (id) {
                case R.id.nav_settings:
                    fragmentClass = SettingsFragment.class;
                    break;

                case R.id.nav_about:
                    fragmentClass = AboutFragment.class;
                    break;

                case R.id.nav_add_city:
                    fragmentClass = SelectCityFragment.class;
                    isRoot = true;
                    break;

            }
        } else {

            fragmentClass = ForecastFragment.class;
            isRoot = true;

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
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment, tag)
                .commit();
    }

    @Nullable
    @Override
    public Toolbar getCommonToolbar() {
        return toolbar;
    }

    @Override
    public void initToolbarNavigationView(Toolbar toolbar, Drawable navigationIcon, boolean globalToolbar, boolean isRoot) {
        if (!globalToolbar || !isRoot) {
            navigationIcon.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.normal_text_color), PorterDuff.Mode.SRC_IN);
            toolbar.setNavigationIcon(navigationIcon);
            toolbar.setContentInsetStartWithNavigation(0);
        } else {
            LogUtils.log(" LOOO G");
            toolbar.setNavigationIcon(null);
        }
    }


    @Override
    public void attachInputListeners() {

    }

    @Override
    public void detachInputListeners() {

    }

    @Override
    public void setFavoritesItems(List<CityUIModel> models) {
        for (CityUIModel cityUIModel : models) {
            navigation.addItemAtPosition(initiateItemField(new WeatherItem(cityUIModel), cityUIModel.name, null), 1);
        }
    }
}
