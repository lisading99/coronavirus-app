package com.example.coronavirusandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import static com.example.coronavirusandroid.MainActivity.MY_PREFS_NAME;

public class NotificationActivity extends AppCompatActivity {
    Button getNotifs;
    TextView country;
    TextView provinceOrState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getNotifs = findViewById(R.id.getNotifs);
        country = findViewById(R.id.country);
        provinceOrState = findViewById(R.id.provinceOrState);

        getNotifs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("country", country.getText().toString());
                editor.putString("province", provinceOrState.getText().toString());
                editor.apply();
                displayNotifsBySpecifiedTime();
            }
        });
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

        calendar.set(Calendar.HOUR_OF_DAY, 1); // For 1 PM or 2 PM
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0,
                new Intent(this, NotificationBroadcast.class), PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
    }
}
