package com.example.coronavirusandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

public class DisplayResultsActivity extends AppCompatActivity {
    MyPagerAdapter adapterViewPager;
    int confirmed;
    int recovered;
    int deaths;
    int thisMonthConfirmed, thisMonthDeaths, thisMonthRecovered, thisWeekConfirmed,
    thisWeekDeaths, thisWeekRecovered;
    String countryOrProvince;
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
        thisMonthConfirmed = intent.getIntExtra("thisMonthConfirmed", 0);
        thisMonthDeaths = intent.getIntExtra("thisMonthDeaths", 0);
        thisMonthRecovered = intent.getIntExtra("thisMonthRecovered", 0);
        thisWeekConfirmed = intent.getIntExtra("thisWeekConfirmed", 0);
        thisWeekDeaths = intent.getIntExtra("thisWeekDeaths", 0);
        thisWeekRecovered = intent.getIntExtra("thisWeekRecovered", 0);
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

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;
        String[] titles = {"Results", "Pie Chart", "Line Graph"};

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TabFragment.newInstance(confirmed, recovered, deaths,
                            thisMonthConfirmed, thisMonthDeaths, thisMonthRecovered,
                            thisWeekConfirmed, thisWeekDeaths, thisWeekRecovered);
                case 1:
                    return PieChartFragment.newInstance(confirmed, recovered, deaths);
                case 2:
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
