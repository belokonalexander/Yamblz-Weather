package com.yamblz.voltek.weather.presentation.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.yamblz.voltek.weather.Navigator;
import com.yamblz.voltek.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends MvpAppCompatFragment implements FragmentContent, CommonToolbarLoader.ToolbarNavigationListener {

    @Nullable
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private Unbinder unbinder;
    public Navigator navigationManager;
    // Holds all disposable with input events subscriptions
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    private CommonToolbarLoader toolbarLoader;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onMenuClick() {
        navigationManager.openNavigationDrawer();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        navigationManager = ((Navigator) getActivity());
        super.onActivityCreated(savedInstanceState);
        toolbarLoader = new CommonToolbarLoader(navigationManager, getFragmentManager(), getContext(), toolbar, this);
        toolbarLoader.initToolbar(getString(getTitle()));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }





}
