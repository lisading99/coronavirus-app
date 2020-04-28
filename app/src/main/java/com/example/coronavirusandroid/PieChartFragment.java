package com.example.coronavirusandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.scales.OrdinalColor;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PieChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int confirmed;
    private int recovered;
    private int deaths;

    public PieChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param confirmed Parameter 1.
     * @param recovered Parameter 2.
     * @return A new instance of fragment PieChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PieChartFragment newInstance(int confirmed, int recovered, int deaths) {
        PieChartFragment fragment = new PieChartFragment();
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
        View view =  inflater.inflate(R.layout.fragment_pie_chart, container, false);
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Confirmed", confirmed));
        data.add(new ValueDataEntry("Recovered", recovered));
        data.add(new ValueDataEntry("Deaths", deaths));
        pie.data(data);
        anyChartView.addFont("Raleway", "file:///android_asset/ralewaysemibold.ttf");
        pie.legend().fontFamily("Raleway");
        pie.labels().fontFamily("Raleway");
        pie.normal().fontFamily("Raleway");
        pie.tooltip().fontFamily("Raleway");
        pie.palette().items(new String[]{"#26C6DA", "#4CAF50", "#FF5722"}, "");
        anyChartView.setChart(pie);
        return view;
    }
}
