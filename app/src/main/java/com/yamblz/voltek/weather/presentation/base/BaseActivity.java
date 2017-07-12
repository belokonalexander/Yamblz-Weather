package com.yamblz.voltek.weather.presentation.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void replaceFragment(BaseFragment fragment, @IdRes int container) {
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fts.replace(container, fragment).commit();
    }
}
