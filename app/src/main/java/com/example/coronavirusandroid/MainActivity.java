package com.example.coronavirusandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CasesByDateApiController.SendCasesToMainActivity
{

    RadioButton countryButton;
    RadioButton provinceButton;
    CasesByDateApiController casesByDateApiController;
    TextInputLayout textInputLayout;
    TextInputEditText countryOrProvinceInput;
    MaterialButton getCoronavirusResults;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static String CHANNEL_ID = "displayResultsNotif";
    public static String MY_PREFS_NAME = "prefName";
    public static String EXTRA_MESSAGE = "com.example.coronavirusandroid.MESSAGE";
    public static String RECOVERED = "com.example.coronavirusandroid.RECOVERED";
    public static String DEATHS = "com.example.coronavirusandroid.DEATHS";
    public static String COUNTRYORPROVINCE = "com.example.coronavirusandroid.COUNTRYORPROVINCE";
    String countryOrProvince;
    Boolean isCountryInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countryOrProvinceInput = findViewById(R.id.countryOrProvinceInput);
        textInputLayout = findViewById(R.id.countryOrProvinceLayout);
        countryButton = findViewById(R.id.countryButton);
        provinceButton = findViewById(R.id.provinceOrStateButton);
        getCoronavirusResults = findViewById(R.id.coronavirusResults);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        casesByDateApiController = new CasesByDateApiController(this);


        // programtically change hint of input depending on which radio button was clicked
        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text field hint to country
                textInputLayout.setHint("Country");

            }
        });

        provinceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change text field hint to province
                textInputLayout.setHint("Province");
            }
        });

        // listen for when text changes for either country or province inputs change and store
        // these changes
        countryOrProvinceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                countryOrProvince = s.toString();

            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch(menuItem.getItemId()) {
                    case R.id.nav_coronavirus_results:
                        intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_notifications:
                        // another startActivity, this is for item with id "menu_item2"
                        intent = new Intent(MainActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        return true;
            }
            return true;
        }});


        getCoronavirusResults.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (countryButton.isChecked()) {
                    isCountryInput = true;

                    // validate inputs from user, invalid inputs display error message
                    //TODO: refactor alert dialog into its own fragment so code is reusable
                    if (!Utils.validateCountryInput(countryOrProvince)) {
                        DialogFragment dialogFragment = AlertDialogFragment.newInstance("" +
                                        "Invalid input",
                                "The given input for country is invalid. Please double " +
                                        "check the spelling " + "for any typos."
                        );
                        dialogFragment.show(getSupportFragmentManager(), "dialog");

                    } else {
                        casesByDateApiController.getCountryData(countryOrProvince, true);
                        isCountryInput = false;
                    }

                } else {
                    try {
                        casesByDateApiController.getProvinceData(countryOrProvince, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void sendCasesPerDay(List<Pair<String, Integer>> casesPerDayList, List<Pair<String, Integer>> deathsPerDayList, List<Pair<String, Integer>> recoveredPerDayList, int confirmedCumulative, int deathsCumulative, int recoveredCumulative) {
        Intent intent = new Intent(MainActivity.this, DisplayResultsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, confirmedCumulative);
        intent.putExtra(RECOVERED, recoveredCumulative);
        intent.putExtra(DEATHS, deathsCumulative);
        int thisMonthConfirmed = Utils.getThisMonthCases(casesPerDayList);
        int thisMonthDeaths = Utils.getThisMonthCases(deathsPerDayList);
        int thisMonthRecovered = Utils.getThisMonthCases(recoveredPerDayList);
        int thisWeekConfirmed = Utils.getThisWeekCases(casesPerDayList);
        int thisWeekDeaths = Utils.getThisWeekCases(deathsPerDayList);
        int thisWeekRecovered = Utils.getThisWeekCases(recoveredPerDayList);
        intent.putExtra("thisMonthConfirmed", thisMonthConfirmed);
        intent.putExtra("thisMonthDeaths", thisMonthDeaths);
        intent.putExtra("thisMonthRecovered", thisMonthRecovered);
        intent.putExtra("thisWeekConfirmed", thisWeekConfirmed);
        intent.putExtra("thisWeekDeaths", thisWeekDeaths);
        intent.putExtra("thisWeekRecovered", thisWeekRecovered);
        intent.putExtra(COUNTRYORPROVINCE, countryOrProvince);
        intent.putExtra("isCountry", isCountryInput);
        startActivity(intent);
    }

    @Override
    public void displayUnavailableData() {
        DialogFragment dialogFragment = AlertDialogFragment.newInstance("Location data unavailable",
                "The given input for country/province data is unavailable. Please " +
                                "try with a different country or province input. "
                );
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
