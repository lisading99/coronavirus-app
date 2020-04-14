package com.example.coronavirusandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CoronavirusApiController.CoronavirusApiControllerToUI {
    EditText country;
    EditText provinceOrState;
    Button getCoronavirusResults;
    CoronavirusApiController coronavirusApiController;
    TextView confirmedText;
    TextView recoverdText;
    TextView deathsText;
    int numConfirmed;
    int numRecovered;
    String CHANNEL_ID;
    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        country = findViewById(R.id.country);
        provinceOrState = findViewById(R.id.provinceOrState);
        getCoronavirusResults = findViewById(R.id.coronavirusResults);
        confirmedText = findViewById(R.id.confirmed);
        recoverdText = findViewById(R.id.recovered);
        deathsText = findViewById(R.id.deaths);
        coronavirusApiController = new CoronavirusApiController(this);


        getCoronavirusResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coronavirusApiController.start(country.getText().toString(), provinceOrState.getText().toString());
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    protected void onStop() {
        super.onStop();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    @Override
    public void updateConfirmed(int confirmed) {
        confirmedText.setText(Integer.toString(confirmed));
        numConfirmed = confirmed;
        CHANNEL_ID = "displayResultsNotif";
        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Coronavirus results")
                .setContentText("confirmed: " + numConfirmed)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        createNotificationChannel();
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
}
