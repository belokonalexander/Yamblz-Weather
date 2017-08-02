package com.yamblz.voltek.weather.presentation.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends MvpAppCompatFragment {

    private Unbinder unbinder;
    private Navigator navigationManager;
    // Holds all disposable with input events subscriptions
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        navigationManager = ((Navigator) getActivity());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected abstract Toolbar getToolbar();

    public void initToolbar(String title) {
        getToolbar().setTitle(title);
        Drawable toolbarNavigationIcon;
        if (getArguments() != null && getArguments().getBoolean(Navigator.NAVIGATION_BACKPRESS)) {
            navigationManager.setNavigationDrawerState(false);
            toolbarNavigationIcon = VectorDrawableCompat.create(getResources(), R.drawable.ic_arrow_back_black_24dp, null);
            getToolbar().setNavigationOnClickListener(v -> getActivity().onBackPressed());
        } else {
            toolbarNavigationIcon = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu_black_24dp, null);
            navigationManager.setNavigationDrawerState(true);
            getToolbar().setNavigationOnClickListener(v -> navigationManager.openNavigationDrawer());
        }

        initToolbarView(toolbarNavigationIcon);

    }

    private void initToolbarView(Drawable navigationIcon) {
        navigationIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.normal_text_color), PorterDuff.Mode.SRC_IN);
        getToolbar().setNavigationIcon(navigationIcon);
        getToolbar().setContentInsetStartWithNavigation(0);
    }

}
