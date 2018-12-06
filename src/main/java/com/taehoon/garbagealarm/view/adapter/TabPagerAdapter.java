package com.taehoon.garbagealarm.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.taehoon.garbagealarm.view.TabFragment1;
import com.taehoon.garbagealarm.view.TabFragment2;
import com.taehoon.garbagealarm.view.TabFragment3;
import com.taehoon.garbagealarm.view.TabLicense;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kth919 on 2017-05-06.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private static String TAG = TabPagerAdapter.class.getName();

    private Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new TabFragment1();
            case 1:
                return new TabFragment2();
            case 2:
                return new TabFragment3();
            case 3:
                return new TabLicense();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        mFragmentMap.put(position, createdFragment);
        Log.d(TAG, "프래그먼트 초기화 위치" + position);
        return createdFragment;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "요일 정보";
            case 1:
                return "알람 설정";
            case 2:
                return "위치 정보";
            case 3:
                return "라이센스";
            default:
                return null;
        }
    }
    public Fragment getRegisteredFragment(int position) {
        return mFragmentMap.get(position);
    }
}
