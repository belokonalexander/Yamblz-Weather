package com.yamblz.voltek.weather.presentation.ui.about;

import android.support.v7.widget.Toolbar;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.presentation.base.BaseFragment;

import butterknife.BindView;

public class AboutFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_about;
    }

    @Override
    protected int getTitle() {
        return R.string.title_about;
    }
}
