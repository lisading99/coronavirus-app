package com.example.coronavirusandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements CoronavirusApiController.CoronavirusApiControllerToUI, NavigationView.OnNavigationItemSelectedListener
{
//    EditText country;
//    EditText provinceOrState;
//    Button getCoronavirusResults;
    Button getNotifs;
    Button displayPieChart;
    CoronavirusApiController coronavirusApiController;
    TextView confirmedText;
    TextView recoverdText;
    TextView deathsText;
    Menu menu;
    TextInputLayout textInputLayout;
    TextInputEditText country, provinceOrState;
    MaterialButton getCoronavirusResults;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    int numConfirmed;
    int numRecovered;
    public static String CHANNEL_ID = "displayResultsNotif";
    public static String MY_PREFS_NAME = "prefName";
    public static String EXTRA_MESSAGE = "com.example.coronavirusandroid.MESSAGE";
    public static String RECOVERED = "com.example.coronavirusandroid.RECOVERED";
    public static String DEATHS = "com.example.coronavirusandroid.DEATHS";
    public static String COUNTRY = "com.example.coronavirusandroid.COUNTRY";
    public static String PROVINCE = "com.example.coronavirusandroid.PROVINCE";
    public static String pieChart = "pie_chart";
    public static String DISPLAY_NUMBERS = "DISPLAY_NUMBERS";
    public static String GET_NOTIFICATIONS = "GET_NOTIFICATIONS";
    NotificationCompat.Builder builder;
    String countryInput;
    String provinceInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        country = findViewById(R.id.country);
        provinceOrState = findViewById(R.id.provinceOrState);
//        country = findViewById(R.id.country);
//        provinceOrState = findViewById(R.id.provinceOrState);
        getCoronavirusResults = findViewById(R.id.coronavirusResults);
        confirmedText = findViewById(R.id.confirmed);
        recoverdText = findViewById(R.id.recovered);
        deathsText = findViewById(R.id.deaths);
        getNotifs = findViewById(R.id.getNotifs);
        displayPieChart = findViewById(R.id.displayPieChart);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        coronavirusApiController = new CoronavirusApiController(this);

        countryInput = country.getText().toString();
        provinceInput = provinceOrState.getText().toString();


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
                    case R.id.nav_latest_news:
                        break;
                    default:
                        return true;
            }
            return true;
        }});


        getCoronavirusResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coronavirusApiController.start(country.getText().toString(), provinceOrState.getText().toString(), DISPLAY_NUMBERS);
            }
        });

        getNotifs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // save country and province of interest for updates and notifs
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("country", country.getText().toString());
                editor.putString("province", provinceOrState.getText().toString());
                editor.apply();
                displayNotifsBySpecifiedTime();
            }
        });

        displayPieChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                String country = prefs.getString("country", "No country defined");//"No name defined" is the default value.
//                String province = prefs.getString("province", "No province defined");
//                coronavirusApiController.start(country.getText().toString(), provinceOrState.getText().toString(), pieChart);
                coronavirusApiController.start(country.getText().toString(), provinceOrState.getText().toString(), pieChart);

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//// notificationId is a unique int for each notification that you must define
//        int notificationId = 1;
//        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    protected void onStop() {
        super.onStop();
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//// notificationId is a unique int for each notification that you must define
//        int notificationId = 1;
//        notificationManager.notify(notificationId, builder.build());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.nav_coronavirus_results:
                intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_notifications:
                // another startActivity, this is for item with id "menu_item2"
                intent = new Intent(this, NotificationActivity.class);
                this.startActivity(intent);
                break;
            case R.id.nav_latest_news:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
    @Override
    public void updateConfirmed(int confirmed) {
        confirmedText.setText(Integer.toString(confirmed));
        numConfirmed = confirmed;

    }

    @Override
    public void updateRecovered(int recovered) {
        recoverdText.setText(Integer.toString(recovered));
        numRecovered = recovered;
    }

    @Override
    public void updateDeaths(int deaths) {
        deathsText.setText(Integer.toString(deaths));
    }

    @Override
    public void updateNotifs(int confirmed, int recovered, int deaths) {
        CHANNEL_ID = "displayResultsNotif";
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Coronavirus results")
                .setContentText("confirmed: " + confirmed)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
// notificationId is a unique int for each notification that you must define
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void displayChart(int confirmed, int recovered, int deaths, String provinceInput, String countryInput) {
        Intent intent = new Intent(getApplicationContext(), DisplayResultsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, confirmed);
        intent.putExtra(RECOVERED, recovered);
        intent.putExtra(DEATHS, deaths);
        intent.putExtra(COUNTRY, countryInput);
        intent.putExtra(PROVINCE, provinceInput);
        System.out.println(provinceInput + "========");
        startActivity(intent);
//        Intent intent = new Intent(getApplicationContext(), PieChartActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, confirmed);
//        intent.putExtra(RECOVERED, recovered);
//        intent.putExtra(DEATHS, deaths);
//        startActivity(intent);
//        Pie pie = AnyChart.pie();
//
//        List<DataEntry> data = new ArrayList<>();
//        data.add(new ValueDataEntry("Confirmed", confirmed));
//        data.add(new ValueDataEntry("Recovered", recovered));
//        data.add(new ValueDataEntry("Deaths", deaths));
//
//        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
//        anyChartView.setChart(pie);
    }

    private void displayNotifsBySpecifiedTime() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String country = prefs.getString("country", "No country defined");//"No name defined" is the default value.
        String province = prefs.getString("province", "No province defined");

//        Calendar calendar = Calendar.getInstance();
//        today.set(Calendar.HOUR_OF_DAY, 1);
//        today.set(Calendar.MINUTE, 12);
//        today.set(Calendar.SECOND, 0);

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 17); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                new Intent(this, NotificationBroadcast.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

// every night at 2am you run your task
//        Timer timer = new Timer();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                coronavirusApiController.start(country, province);
//            }
//        };
//        timer.schedule(timerTask, today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CoronavirusNotifs";
            String description = "notif for coronavirus results";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
