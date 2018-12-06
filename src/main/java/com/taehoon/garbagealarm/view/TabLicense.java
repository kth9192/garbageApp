package com.taehoon.garbagealarm.view;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taehoon.garbagealarm.R;
import com.taehoon.garbagealarm.databinding.TabFragmentLicenseBinding;

/**
 * Created by kth919 on 2017-10-17.
 */

public class TabLicense extends Fragment {

    private static String TAG = TabLicense.class.getName();
    TabFragmentLicenseBinding tabFragmentLicenseBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        tabFragmentLicenseBinding = DataBindingUtil.inflate(inflater, R.layout.tab_fragment_license, container, false);
        tabFragmentLicenseBinding.setTabLicense(this);
        View view = tabFragmentLicenseBinding.getRoot();

        return view;
    }
}
