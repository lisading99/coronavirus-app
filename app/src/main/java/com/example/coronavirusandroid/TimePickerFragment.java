package com.example.coronavirusandroid;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimePickerFragment#} factory method to
 * create an instance of this fragment.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    sendTimeToNotificationActivity sendTime;
    public TimePickerFragment (sendTimeToNotificationActivity sendTime) {
       this.sendTime = sendTime;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // send hour of day and minute to NotificationActivity to send notifs to user

        Intent intent = new Intent(getContext(), NotificationActivity.class);
        intent.putExtra("hour", hourOfDay);
        intent.putExtra("minute", minute);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("NotificationActivity", 1);
        String time = parseTimeTo12HourFormat(hourOfDay, minute);
        sendTime.sendHourMinuteToNotificationActivity(time, hourOfDay, minute);
    }

    public static String parseTimeTo12HourFormat(int hour, int minute) {
        String hourAsString = Integer.toString(hour);
        String minuteAsString = Integer.toString(minute);
        String timeAsString;
        if (minuteAsString.length() == 1) {
            String prevMinuteAsString = minuteAsString;
            minuteAsString = "0" + prevMinuteAsString;
        }
        if (hourAsString.length() == 2) {
            // translate to 12 hour format
            hourAsString = Integer.toString(hour - 12);
            timeAsString = hourAsString + ":" + minuteAsString + " PM";
        } else {
            if (hour == 0) {
                hourAsString = Integer.toString(12);
            }
            timeAsString = hourAsString + ":" + minuteAsString + " AM";
        }
        return timeAsString;
    }

    public interface sendTimeToNotificationActivity {
        void sendHourMinuteToNotificationActivity(String time, int hour, int minute);
    }
}
