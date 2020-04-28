package com.example.coronavirusandroid;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.util.Pair;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.coronavirusandroid.MainActivity.CHANNEL_ID;
import static com.example.coronavirusandroid.MainActivity.MY_PREFS_NAME;

public class NotificationBroadcast extends BroadcastReceiver implements CasesByDateApiController.SendCasesToNotificationBroadcast {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//        String country = prefs.getString("country", "No country defined");//"No name defined" is the default value.
//        String province = prefs.getString("province", "No province defined");
        String countryOrProvince = prefs.getString("countryOrProvince", "");
        boolean isCountry = prefs.getBoolean("isCountry", true);
        CasesByDateApiController casesByDateApiController = new CasesByDateApiController(this);
        if (isCountry) {
            casesByDateApiController.getCountryData(countryOrProvince, true);
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
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void updateNotifs(int confirmed, int recovered, int deaths) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Coronavirus results")
//                .setContentText("confirmed: " + confirmed)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//        createNotificationChannel();
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//// notificationId is a unique int for each notification that you must define
//        int notificationId = 1;
//        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void sendCasesPerDay(List<Pair<String, Integer>> casesPerDayList, List<Pair<String, Integer>> deathsPerDayList, List<Pair<String, Integer>> recoveredPerDayList) {

    }

    @Override
    public void sendCumulativeCases(int confirmed, int deaths, int recovered) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Coronavirus results")
                .setContentText("confirmed: " + confirmed + "\n" +
                        "recovered: " + recovered + "\n" +
                        "deaths: " + deaths)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
// notificationId is a unique int for each notification that you must define
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }
}
