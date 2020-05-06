package com.example.coronavirusandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment {
    private int confirmed;
    private int recovered;
    private int deaths;
    private int todayConfirmed, todayRecovered, todayDeaths;
    private int thisMonthConfirmed, thisMonthDeaths, thisMonthRecovered, thisWeekConfirmed,
    thisWeekDeaths, thisWeekRecovered;

    public TabFragment() {
        // Required empty public constructor
    }

    public static TabFragment newInstance(int confirmed, int recovered, int deaths,
                                          int thisMonthConfirmed, int thisMonthDeaths,
                                          int thisMonthRecovered, int thisWeekConfirmed,
                                          int thisWeekDeaths, int thisWeekRecovered) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt("confirmed", confirmed);
        args.putInt("recovered", recovered);
        args.putInt("deaths", deaths);
        args.putInt("thisMonthConfirmed", thisMonthConfirmed);
        args.putInt("thisMonthRecovered", thisMonthRecovered);
        args.putInt("thisMonthDeaths", thisMonthDeaths);
        args.putInt("thisWeekConfirmed", thisWeekConfirmed);
        args.putInt("thisWeekDeaths", thisWeekDeaths);
        args.putInt("thisWeekRecovered", thisWeekRecovered);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            confirmed = getArguments().getInt("confirmed");
            recovered = getArguments().getInt("recovered");
            deaths = getArguments().getInt("deaths");
            thisMonthConfirmed = getArguments().getInt("thisMonthConfirmed");
            thisMonthDeaths = getArguments().getInt("thisMonthDeaths");
            thisMonthRecovered = getArguments().getInt("thisMonthRecovered");
            thisWeekConfirmed = getArguments().getInt("thisWeekConfirmed");
            thisWeekDeaths = getArguments().getInt("thisWeekDeaths");
            thisWeekRecovered = getArguments().getInt("thisWeekRecovered");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab, container, false);
        TextView confirmedNumber = view.findViewById(R.id.confirmedNumber);
        TextView recoveredNumber = view.findViewById(R.id.recoveredNumber);
        TextView deathNumber = view.findViewById(R.id.deathsNumber);
        TextView thisMonthDeathNumber = view.findViewById(R.id.thisMonthDeathsNumber);
        TextView thisMonthConfirmedNumber = view.findViewById(R.id.thisMonthConfirmedNumber);
        TextView thisMonthRecoveredNumber = view.findViewById(R.id.thisMonthRecoveredNumber);
        TextView thisWeekConfirmedNumber = view.findViewById(R.id.thisWeekConfirmedNumber);
        TextView thisWeekDeathsNumber = view.findViewById(R.id.thisWeekDeathsNumber);
        TextView thisWeekRecoveredNumber = view.findViewById(R.id.thisWeekRecoveredNumber);
        confirmedNumber.setText(Integer.toString(confirmed));
        recoveredNumber.setText(Integer.toString(recovered));
        deathNumber.setText(Integer.toString(deaths));
        thisMonthConfirmedNumber.setText(Integer.toString(thisMonthConfirmed));
        thisMonthDeathNumber.setText(Integer.toString(thisMonthDeaths));
        thisMonthRecoveredNumber.setText(Integer.toString(thisMonthRecovered));
        thisWeekConfirmedNumber.setText(Integer.toString(thisWeekConfirmed));
        thisWeekDeathsNumber.setText(Integer.toString(thisWeekDeaths));
        thisWeekRecoveredNumber.setText(Integer.toString(thisWeekRecovered));
        return view;
    }
}
