package com.example.coronavirusandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.google.android.material.tabs.TabLayout;

public class DisplayResultsActivity extends AppCompatActivity {
    ViewPager pager;
    View view;
    PagerAdapter pagerAdapter;
    MyPagerAdapter adapterViewPager;

    int confirmed;
    int recovered;
    int deaths;
    String countryOrProvince;
    String province;
    boolean isCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);
        Intent intent = getIntent();
        confirmed = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
        recovered = intent.getIntExtra(MainActivity.RECOVERED, 0);
        deaths = intent.getIntExtra(MainActivity.DEATHS, 0);
        isCountry = intent.getBooleanExtra("isCountry", false);
//        country = intent.getStringExtra(MainActivity.COUNTRY);
//        province = intent.getStringExtra(MainActivity.PROVINCE);
        countryOrProvince = intent.getStringExtra(MainActivity.COUNTRYORPROVINCE);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        tabs.setupWithViewPager(vpPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_grade_black_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.ic_pie_chart_black_24dp);
        tabs.getTabAt(2).setIcon(R.drawable.ic_show_chart_black_24dp);

    }

//    @Override
//    protected  void onResume() {
//        super.onResume();
//        Intent intent = getIntent();
//        confirmed = intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0);
//        recovered = intent.getIntExtra(MainActivity.RECOVERED, 0);
//        deaths = intent.getIntExtra(MainActivity.DEATHS, 0);
//        countryOrProvince = intent.getStringExtra(MainActivity.COUNTRYORPROVINCE);
//        isCountry = intent.getBooleanExtra("isCountry", false);
//    }
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;
        String[] titles = {"Results", "Pie Chart", "Line Graph"};

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return TabFragment.newInstance(confirmed, recovered, deaths);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return PieChartFragment.newInstance(confirmed, recovered, deaths);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return BarGraphFragment.newInstance(countryOrProvince, isCountry);
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }

}
