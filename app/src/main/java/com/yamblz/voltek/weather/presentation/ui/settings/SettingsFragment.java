package com.yamblz.voltek.weather.presentation.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.yamblz.voltek.weather.R;
import com.yamblz.voltek.weather.data.storage.StorageRepository;
import com.yamblz.voltek.weather.presentation.base.BasePreferenceFragment;

public class SettingsFragment extends BasePreferenceFragment {

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(StorageRepository.CATEGORY_SETTINGS);
        setPreferencesFromResource(R.xml.app_prefs, rootKey);
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public int getTitle() {
        return R.string.nav_settings;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.title_settings);
    }


}
