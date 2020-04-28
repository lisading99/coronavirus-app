package com.example.coronavirusandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.example.coronavirusandroid.MainActivity.MY_PREFS_NAME;

public class NotificationActivity extends AppCompatActivity implements TimePickerFragment.sendTimeToNotificationActivity,
CasesByDateApiController.SendCasesToNotificationActivity{
    Button getNotifs;
    Button setTime;
    RadioButton countryButton, provinceButton;
//    TextView setTime;
    TextView country;
    TextView provinceOrState;
    TextInputLayout textInputLayout;
    TextInputEditText countryOrProvinceInput;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    int hour, minute, calledFrom;
    CasesByDateApiController casesByDateApiController;
    AlertDialog.Builder alertBuilder;
    AlertDialog alert;
    String countryOrProvince;
    boolean isCountryInput = true;
    boolean isResponseSuccessful;
    String time;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getNotifs = findViewById(R.id.getNotifs);
        setTime = findViewById(R.id.setTime);
        countryButton = findViewById(R.id.countryButton);
        provinceButton = findViewById(R.id.provinceOrStateButton);
        countryOrProvinceInput = findViewById(R.id.countryOrProvinceInput);
        textInputLayout = findViewById(R.id.countryOrProvinceLayout);
//        country = findViewById(R.id.country);
//        provinceOrState = findViewById(R.id.provinceOrState);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        isResponseSuccessful = true;


        casesByDateApiController= new CasesByDateApiController(this);
        DialogFragment newFragment = new TimePickerFragment(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch(menuItem.getItemId()) {
                    case R.id.nav_coronavirus_results:
                        intent = new Intent(NotificationActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_notifications:
                        // another startActivity, this is for item with id "menu_item2"
                        intent = new Intent(NotificationActivity.this, NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_latest_news:
                        break;
                    default:
                        return true;
                }
                return true;
            }});

        // programtically change hint of input depending on which radio button was clicked
        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change text field hint to country
                textInputLayout.setHint("Country");
                isCountryInput = true;
            }
        });

        provinceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change text field hint to province
                textInputLayout.setHint("Province");
                isCountryInput = false;
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

//        provinceOrState.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                provinceInput = s.toString();
//            }
//        });

        final Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        // parse it so that it's 12 hour format
        String currentTime = TimePickerFragment.parseTimeTo12HourFormat(currentHour, currentMinute);
        setTime.setText(currentTime);

        // set hour, min to default current time
        hour = currentHour - 12;
        minute = currentMinute;
        this.time = currentTime;

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });

        getNotifs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isCountryInput) {
                    if (! Utils.validateCountryInput(countryOrProvince)) {
                        alertBuilder = new AlertDialog.Builder(NotificationActivity.this);
                        alertBuilder.setTitle("Invalid input")
                                .setMessage("The given input for country is invalid. Please double check the spelling " +
                                        "for any typos.");
                        alertBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        alert = alertBuilder.create();
                        alert.show();
                    } else {
                        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("countryOrProvince", countryOrProvince);
                        editor.putBoolean("isCountry", isCountryInput);
                        editor.apply();
                        casesByDateApiController.getCountryData(countryOrProvince, true);
                        if (isResponseSuccessful) {
                            displayNotifsBySpecifiedTime();
                        }
                    }

                    // validate inputs from user, invalid inputs display error message
                    //TODO: refactor alert dialog into its own fragment so code is reusable
//                    if (!Utils.validateCountryInput(countryOrProvince)) {
//                        alertBuilder = new AlertDialog.Builder(MainActivity.this);
//                        alertBuilder.setTitle("Invalid input")
//                                .setMessage("The given input for country is invalid. Please double check the spelling " +
//                                        "for any typos.");
//                        alertBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                return;
//                            }
//                        });
//
//                        alert = alertBuilder.create();
//                        alert.show();
//                    } else {
//                        casesByDateApiController.getCountryData(countryOrProvince, true);
//                        isCountryInput = false;
//                    }

                } else {
                    try {
                        casesByDateApiController.getProvinceData(countryOrProvince, true);
                        if (isResponseSuccessful) {
                            editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("countryOrProvince", countryOrProvince);
                            editor.putBoolean("isCountry", isCountryInput);
                            editor.apply();
                            displayNotifsBySpecifiedTime();
                        }
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
    protected  void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTime.setText(String.format("%d: %d", hour, minute));
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);

        // parse it so that it's 12 hour format
        String currentTime = TimePickerFragment.parseTimeTo12HourFormat(currentHour, currentMinute);
        this.time = currentTime;
        setTime.setText(currentTime);
    }

    private void displayNotifsBySpecifiedTime() {

//        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        String country = prefs.getString("country", "No country defined");//"No name defined" is the default value.
//        String province = prefs.getString("province", "No province defined");

        // check if input will produce any results from api; if not display dialog alert
//        casesByDateApiController.getProvinceData();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                new Intent(this, NotificationBroadcast.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        // display message that notificaiton has been set
        Toast toast = Toast.makeText(getApplicationContext(), "Notification has been set to send coronavirus updates" +
                " at " + time + " every day.", Toast.LENGTH_LONG);
        View view = toast.getView();

//Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(getResources().getColor(R.color.secondaryColor), PorterDuff.Mode.SRC_IN);

//Gets the TextView from the Toast so it can be editted
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(getResources().getColor(R.color.colorControlActivated));
        toast.show();
    }

    @Override
    public void sendHourMinuteToNotificationActivity(String time, int hourOfDay, int minute) {
        setTime.setText(time);
        this.time = time;
        this.hour = hourOfDay;
        this.minute = minute;
    }

    @Override
    public void sendCasesPerDay(List<Pair<String, Integer>> casesPerDayList, List<Pair<String, Integer>> deathsPerDayList, List<Pair<String, Integer>> recoveredPerDayList) {

    }

    @Override
    public void sendCumulativeCases(int confirmed, int deaths, int recovered) {

    }

    @Override
    public void displayUnavailableData() {
        isResponseSuccessful = false;
        alertBuilder = new AlertDialog.Builder(NotificationActivity.this);
        alertBuilder.setTitle("Location data unavailable")
                .setMessage("The given input for country/province data is unavailable. Please" +
                        "try with a different country or province input. ");
        alertBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alert = alertBuilder.create();
        alert.show();
    }
}
