package com.taehoon.garbagealarm;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.taehoon.garbagealarm.databinding.ActivityTutorialBinding;
import com.taehoon.garbagealarm.databinding.FragmentTutorialBinding;
import com.taehoon.garbagealarm.utils.Utils;

public class TutorialActivity extends AppCompatActivity {

    private static String TAG = TutorialActivity.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    final ArgbEvaluator evaluator = new ArgbEvaluator();

    private int pageNum = 0;
    private ImageView[] indicators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTutorialBinding activityTutorialBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutorial);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link androidx.viewpager.widget.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      androidx.fragment.app.FragmentStatePagerAdapter.
     */ /**
         * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
         * fragments for each of the sections. We use a
         * {@link FragmentPagerAdapter} derivative, which will keep every
         * loaded fragment in memory. If this becomes too memory intensive, it
         * may be best to switch to a
         * androidx.fragment.app.FragmentStatePagerAdapter.
         */

        int color1 = ResourcesCompat.getColor(getResources(), R.color.tutorial_one, null);
        int color2 = ResourcesCompat.getColor(getResources(), R.color.tutorial_two, null);
        int color3 = ResourcesCompat.getColor(getResources(), R.color.tutorial_three, null);

        final int[] colorList = new int[]{color1, color2, color3};

        indicators = new ImageView[]{activityTutorialBinding.introIndicator0,
                activityTutorialBinding.introIndicator1, activityTutorialBinding.introIndicator2};

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        activityTutorialBinding.container.setAdapter(mSectionsPagerAdapter);
        activityTutorialBinding.container.setCurrentItem(pageNum);
        updateIndicators(pageNum);

        activityTutorialBinding.container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         /*
         color update
          */
                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position], colorList[position == 2 ? position : position + 1]);
                activityTutorialBinding.container.setBackgroundColor(colorUpdate);

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                getWindow().setStatusBarColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position) {

                pageNum = position;
                updateIndicators(pageNum);

                switch (position) {
                    case 0:
                        activityTutorialBinding.container.setBackgroundColor(color1);
                        break;
                    case 1:
                        activityTutorialBinding.container.setBackgroundColor(color2);
                        break;
                    case 2:
                        activityTutorialBinding.container.setBackgroundColor(color3);
                        break;
                }

                activityTutorialBinding.introBtnNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                activityTutorialBinding.introBtnFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Glide.with(this).load(R.drawable.right_arrow_white).into(activityTutorialBinding.introBtnNext);

        activityTutorialBinding.introBtnNext.setOnClickListener(v -> {
            pageNum += 1;
            activityTutorialBinding.container.setCurrentItem(pageNum, true);
        });

        activityTutorialBinding.introBtnSkip.setOnClickListener(v -> {
            finish();
            Utils.saveSharedSetting(this, MainActivity.PREF_USER_FIRST_TIME, "false");
        });

        activityTutorialBinding.introBtnFinish.setOnClickListener(v -> {
            finish();
            Utils.saveSharedSetting(this, MainActivity.PREF_USER_FIRST_TIME, "false");
        });
    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        int[] bgs = new int[]{R.drawable.tutorial1, R.drawable.tutorial2, R.drawable.tutorial3};
        int[] strs = new int[]{R.string.tutorial_str_one, R.string.tutorial_str_two, R.string.tutorial_str_three};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            FragmentTutorialBinding fragmentTutorialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tutorial, container, false);

            fragmentTutorialBinding.sectionLabel.setText(strs[getArguments().getInt(ARG_SECTION_NUMBER) -1 ]);

            Glide.with(this).load(bgs[getArguments().getInt(ARG_SECTION_NUMBER) - 1]).into(fragmentTutorialBinding.img);

            return fragmentTutorialBinding.getRoot();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }
}
