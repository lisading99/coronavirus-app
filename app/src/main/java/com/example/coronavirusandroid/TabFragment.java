package com.example.coronavirusandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int confirmed;
    private int recovered;
    private int deaths;

    public TabFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TabFragment newInstance(int confirmed, int recovered, int deaths) {
        TabFragment fragment = new TabFragment();
        Bundle args = new Bundle();
        args.putInt("confirmed", confirmed);
        args.putInt("recovered", recovered);
        args.putInt("deaths", deaths);
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tab, container, false);
        TextView confirmedNumber = view.findViewById(R.id.confirmedNumber);
        TextView recoveredNumber = view.findViewById(R.id.recoveredNumber);
        TextView deathNumber = view.findViewById(R.id.deathsNumber);
        confirmedNumber.setText(Integer.toString(confirmed));
        recoveredNumber.setText(Integer.toString(recovered));
        deathNumber.setText(Integer.toString(deaths));
        return view;
    }
}
