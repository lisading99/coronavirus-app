package com.example.coronavirusandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity implements CoronavirusApiController.CoronavirusApiControllerToUI {
    AnyChartView anyChartView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piechart_activity);
        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);

        Pie pie = AnyChart.pie();

        Intent intent = getIntent();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Confirmed", intent.getIntExtra(MainActivity.EXTRA_MESSAGE, 0)));
        data.add(new ValueDataEntry("Recovered", intent.getIntExtra(MainActivity.RECOVERED, 0)));
        data.add(new ValueDataEntry("Deaths", intent.getIntExtra(MainActivity.DEATHS, 0)));
        pie.data(data);
        anyChartView.setChart(pie);
    }

    @Override
    public void updateConfirmed(int confirmed) {

    }

    @Override
    public void updateRecovered(int recovered) {

    }

    @Override
    public void updateDeaths(int deaths) {

    }

    @Override
    public void updateNotifs(int confirmed, int recovered, int deaths) {

    }

    @Override
    public void displayChart(int confirmed, int recovered, int deaths, String province, String country) {

    }
}
