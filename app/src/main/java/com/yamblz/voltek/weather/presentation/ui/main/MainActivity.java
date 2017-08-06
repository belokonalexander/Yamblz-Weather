package com.yamblz.voltek.weather.presentation.ui.main;


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
import android.widget.Toast;

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
import com.yamblz.voltek.weather.domain.exception.DeleteLastCityException;
import com.yamblz.voltek.weather.presentation.base.BaseActivity;
import com.yamblz.voltek.weather.presentation.ui.menu.items.MainDrawerItem;
import com.yamblz.voltek.weather.presentation.ui.menu.items.WeatherItem;
import com.yamblz.voltek.weather.utils.StringUtils;
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
                .withSelectedItem(-1)
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
    public void navigateTo(Class<? extends Fragment> where, boolean asRoot, String tag) {

        //TODO if this item already selected
        if (getSupportFragmentManager().findFragmentByTag(tag) != null) {
            navigation.closeDrawer();
            return;
        }

        Fragment fragment = Fragment.instantiate(getBaseContext(), where.getName());

        if (!singlePane || asRoot) {
            openAsRoot(fragment, tag);
        } else {
            openWithBackStack(fragment, tag);
        }
    }

    @Override
    public void showDialogForCity(CityUIModel selectedCity) {
        CityDialog cityDialog = new CityDialog(selectedCity, dialog -> weatherPresenter.deleteCityFromFavorite(selectedCity));
        cityDialog.show(getSupportFragmentManager(), "showDialogForCity");
    }

    @Override
    public void showError(DeleteLastCityException e) {
        Toast.makeText(this, StringUtils.fromError(e), Toast.LENGTH_SHORT).show();
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
    public void initToolbarNavigationView(Toolbar toolbar, Drawable navigationIcon) {
        if (singlePane) {
            navigationIcon.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.normal_text_color), PorterDuff.Mode.SRC_IN);
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

        int pos = 1;
        for (CityUIModel cityUIModel : models) {
            PrimaryDrawerItem drawerItem = new WeatherItem(cityUIModel);

            //skip existed items and add original items
            if (navigation.getPosition(drawerItem) < 0) {
                navigation.addItemAtPosition(initiateItemField(drawerItem, cityUIModel.name, null), pos);
            } else {

                //remove from future deleted scope
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
        //cause I can't click on item without select it, in despite of I disable selection for this item early %)
        if (singlePane) {
            navigation.deselect();
        }
    }

    @Override
    public void selectStickyItem() {

    }


}
