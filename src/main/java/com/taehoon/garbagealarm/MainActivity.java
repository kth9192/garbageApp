package com.taehoon.garbagealarm;

import androidx.databinding.DataBindingUtil;
import android.graphics.Typeface;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taehoon.garbagealarm.databinding.ActivityMainBinding;
import com.taehoon.garbagealarm.view.adapter.TabPagerAdapter;
import com.taehoon.garbagealarm.view.helper.AlarmHelper;

public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getName();

    private ActivityMainBinding activityMainBinding;
    private AlarmHelper alarmHelper;
    private TabPagerAdapter mPageAdapter;
    private Fragment fragment;

    private int tmpPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setExitTransition(new Explode());

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.addAlarm.setOnClickListener(alarmListener);

        mPageAdapter = new TabPagerAdapter(getSupportFragmentManager());
        activityMainBinding.tabViewPager.setAdapter(mPageAdapter);
        activityMainBinding.tabLayout.setupWithViewPager(activityMainBinding.tabViewPager);
        activityMainBinding.tabLayout.addOnTabSelectedListener(tabSelectedListener);
        TabLayout.Tab tab = activityMainBinding.tabLayout.getTabAt(0);
        if (tab != null) {
            tab.select();
        }

        activityMainBinding.tabViewPager.addOnPageChangeListener(tabPageChangeListener);
    }

    public void addTabIcon(){
        activityMainBinding.tabLayout.getTabAt(0).setIcon(R.drawable.ic_date_range_white_24dp);
        activityMainBinding.tabLayout.getTabAt(1).setIcon(R.drawable.ic_alarm_white_24dp);
        activityMainBinding.tabLayout.getTabAt(2).setIcon(R.drawable.ic_event_note_white_24dp);
        activityMainBinding.tabLayout.getTabAt(3).setIcon(R.drawable.ic_info_white_24dp);
    }

    View.OnClickListener alarmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            alarmHelper = new AlarmHelper(MainActivity.this, getApplicationContext());

            fragment = mPageAdapter.getRegisteredFragment(tmpPosition);
            alarmHelper.showDialog();
        }
    };

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener(){

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            LinearLayout tabLayout1 = (LinearLayout)((ViewGroup) activityMainBinding.tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
            TextView tabTextView = (TextView) tabLayout1.getChildAt(1);
            tabTextView.setTypeface(null, Typeface.BOLD);
            tabTextView.setTextSize(20);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            LinearLayout tabLayout1 = (LinearLayout)((ViewGroup) activityMainBinding.tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
            TextView tabTextView = (TextView) tabLayout1.getChildAt(1);
            tabTextView.setTypeface(null, Typeface.NORMAL);
            tabTextView.setTextSize(16);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            Log.d(TAG, "탭위치" + tab.getPosition());
            LinearLayout tabLayout1 = (LinearLayout)((ViewGroup) activityMainBinding.tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
            TextView tabTextView = (TextView) tabLayout1.getChildAt(1);
            tabTextView.setTypeface(null, Typeface.BOLD);
        }
    };

    ViewPager.OnPageChangeListener tabPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            tmpPosition = position;
            activityMainBinding.tabLayout.getTabAt(position).select();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}