package com.yamblz.voltek.weather.presentation.ui.main;


import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.WeatherApp;
import com.yamblz.voltek.weather.domain.entity.CityUIModel;
import com.yamblz.voltek.weather.domain.exception.DeleteLastCityException;
import com.yamblz.voltek.weather.presentation.base.BaseActivity;
import com.yamblz.voltek.weather.presentation.ui.menu.items.MainDrawerItem;
import com.yamblz.voltek.weather.presentation.ui.menu.items.WeatherItem;
import com.yamblz.voltek.weather.utils.ResourceUtils;
import com.yamblz.voltek.weather.utils.SimpleMap;
import com.yamblz.voltek.weather.utils.classes.SetWithSelection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements Navigator, WeatherView {

    @InjectPresenter
    WeatherPresenter weatherPresenter;

    @ProvidePresenter
    WeatherPresenter provideFavoritesPresenter() {
        return WeatherApp.get(this).getAppComponent().getFavoritesPresenter();
    }

    private Drawer navigation;

    @Nullable
    @BindView(R.id.drawer_content)
    ViewGroup navigationContainer;

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
                .withScrollToTopAfterClick(false)
                .withSelectedItem(-1)
                .withSliderBackgroundColorRes(R.color.drawer_item_background_color)
                .withDisplayBelowStatusBar(true)
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    onDrawerItemClick(drawerItem, false);
                    return true;
                })
                .withOnDrawerItemLongClickListener((view, position, drawerItem) -> {
                    onDrawerItemClick(drawerItem, true);
                    return true;
                });


        drawerBuilder.withStickyDrawerItems(customInflateMenu(R.menu.sticky_footer_drawer));

        if (!singlePane) {
            drawerBuilder.withStickyHeader(R.layout.drawer_header_layout);
            navigation = drawerBuilder.buildView();
            navigationContainer.addView(navigation.getSlider());
        } else {
            navigation = drawerBuilder.build();
        }
    }

    @SuppressLint("RestrictedApi")
    private List<IDrawerItem> customInflateMenu(@MenuRes int menuId) {

        SupportMenuInflater menuInflater = new SupportMenuInflater(this);
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

    private IDrawerItem initiateItemField(SecondaryDrawerItem drawerItem, String title, Drawable icon) {
        return drawerItem.withName(title)
                .withIcon(icon)
                .withIconTintingEnabled(true)
                .withIconColor(ContextCompat.getColor(getBaseContext(), R.color.drawer_secondary_icon))
                .withSelectedIconColor(ContextCompat.getColor(getBaseContext(), R.color.drawer_secondary_icon_selected));
    }

    private IDrawerItem initiateItemField(PrimaryDrawerItem drawerItem, String title, Drawable icon) {
        return drawerItem.withName(title)
                .withIcon(icon)
                .withIconTintingEnabled(true)
                .withSelectable(!singlePane)
                .withSelectedIconColor(ContextCompat.getColor(getBaseContext(), R.color.material_drawer_dark_selected))
                .withIdentifier(((MainDrawerItem) drawerItem).getItemId());
    }


    private void onDrawerItemClick(IDrawerItem drawerItem, boolean longClick) {

        //individual item of menu
        if (drawerItem instanceof MainDrawerItem) {
            int id = ((MainDrawerItem) drawerItem).getItemId();
            switch (id) {
                case R.id.nav_settings:
                    weatherPresenter.navigateToSettings();
                    break;

                case R.id.nav_about:
                    weatherPresenter.navigateToAbout();
                    break;

                case R.id.nav_add_city:
                    weatherPresenter.navigateToAddCity();
                    break;

            }

        } else {
            weatherPresenter.weatherClick(drawerItem, longClick);
            if (!longClick)
                navigation.closeDrawer();
        }

    }


    @Override
    public void navigateTo(Class<? extends Fragment> where, boolean asRoot, String tag, SimpleMap bundle) {

        //if this item is already selected
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            navigation.closeDrawer();
            return;
        }

        Fragment fragment = Fragment.instantiate(getBaseContext(), where.getName());

        if (bundle != null) {
            fragment.setArguments(SimpleMap.toBundle(bundle));
        }

        if (!singlePane || asRoot) {
            openAsRoot(fragment, tag);
        } else {
            openWithBackStack(fragment, tag);
        }
    }

    @Override
    public void showDialogForCity(CityUIModel selectedCity) {
        CityDialog cityDialog = new CityDialog();
        cityDialog.initData(selectedCity, dialog -> weatherPresenter.deleteCityFromFavorite(selectedCity));
        cityDialog.show(getSupportFragmentManager(), "showDialogForCity");
    }

    @Override
    public void showError(DeleteLastCityException e) {
        Toast.makeText(this, ResourceUtils.fromError(e), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void scrollToElement(WeatherItem item) {
        int pos = navigation.getPosition(item);
        navigation.getRecyclerView().scrollToPosition(pos);
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
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.main_content, fragment, tag)
                .show(fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openAsRoot(Fragment fragment, String tag) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .replace(R.id.main_content, fragment, tag)
                .commit();
    }

    @Override
    public void initToolbarNavigationView(Toolbar toolbar, Drawable navigationIcon) {
        if (singlePane) {
            navigationIcon.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.title_text_color), PorterDuff.Mode.SRC_IN);
            toolbar.setNavigationIcon(navigationIcon);
            toolbar.setContentInsetStartWithNavigation(0);
        } else {
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
    public void setFavoritesItems(SetWithSelection<CityUIModel> models) {

        List<Long> forDelete = new ArrayList<>();
        for (IDrawerItem item : navigation.getDrawerItems())
            forDelete.add(item.getIdentifier());

        int pos = 0;
        for (CityUIModel cityUIModel : models) {
            SecondaryDrawerItem drawerItem = new WeatherItem(cityUIModel);

            //skip existed items and add original items
            if (navigation.getPosition(drawerItem) < 0) {
                navigation.addItemAtPosition(initiateItemField(drawerItem, cityUIModel.name,
                        VectorDrawableCompat.create(getResources(), R.drawable.ic_location_city_black_24dp, null)), pos);
            } else {

                //remove from future delete scope
                forDelete.remove(drawerItem.getIdentifier());
            }
            pos++;
        }

        for (Long id : forDelete) {
            navigation.removeItem(id);
        }

    }

    @Override
    public void selectWeatherItem(WeatherItem item) {
        navigation.setSelection(item, false);
    }

    @Override
    public void selectStickyItem() {

    }

    @Override
    public void deleteWeatherItem(WeatherItem item) {
        navigation.removeItem(item.getIdentifier());
    }

}
