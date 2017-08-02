package com.yamblz.voltek.weather.presentation.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.MvpDelegate;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;


public abstract class BasePreferenceFragment extends PreferenceFragmentCompat {


    private Unbinder unbinder;
    // Holds all disposable with input events subscriptions
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    protected abstract Toolbar getToolbar();
    private Navigator navigationManager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
    }

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationManager = (Navigator) getActivity();
    }


    private boolean mIsStateSaved;
    private MvpDelegate<? extends BasePreferenceFragment> mMvpDelegate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        mIsStateSaved = false;

        getMvpDelegate().onAttach();
    }

    public void onResume() {
        super.onResume();

        mIsStateSaved = false;

        getMvpDelegate().onAttach();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mIsStateSaved = true;

        getMvpDelegate().onSaveInstanceState(outState);
        getMvpDelegate().onDetach();
    }

    @Override
    public void onStop() {
        super.onStop();

        getMvpDelegate().onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getMvpDelegate().onDetach();
        getMvpDelegate().onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //We leave the screen and respectively all fragments will be destroyed
        if (getActivity().isFinishing()) {
            getMvpDelegate().onDestroy();
            return;
        }

        // When we rotate device isRemoving() return true for fragment placed in backstack
        // http://stackoverflow.com/questions/34649126/fragment-back-stack-and-isremoving
        if (mIsStateSaved) {
            mIsStateSaved = false;
            return;
        }

        // See https://github.com/Arello-Mobile/Moxy/issues/24
        boolean anyParentIsRemoving = false;
        Fragment parent = getParentFragment();
        while (!anyParentIsRemoving && parent != null) {
            anyParentIsRemoving = parent.isRemoving();
            parent = parent.getParentFragment();
        }

        if (isRemoving() || anyParentIsRemoving) {
            getMvpDelegate().onDestroy();
        }
    }

    /**
     * @return The {@link MvpDelegate} being used by this Fragment.
     */
    public MvpDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpDelegate<>(this);
        }

        return mMvpDelegate;
    }

    protected void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
