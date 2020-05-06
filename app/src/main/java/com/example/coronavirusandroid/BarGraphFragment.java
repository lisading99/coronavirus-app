package com.example.coronavirusandroid;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BarGraphFragment extends Fragment implements CasesByDateApiController.SendCasesToBarGraphFragment{
    String country;
    String countryOrProvince;
    boolean isCountry;
    LinkedHashMap<String, Integer> casesPerMonth = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> deathsPerMonth = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> recoveredPerMonth = new LinkedHashMap<>();

    public BarGraphFragment() {
        // Required empty public constructor
    }

    public static BarGraphFragment newInstance(String countryOrProvince, Boolean isCountry) {
        BarGraphFragment fragment = new BarGraphFragment();
        Bundle args = new Bundle();
        args.putString("countryOrProvince", countryOrProvince);
        args.putBoolean("isCountry", isCountry);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            countryOrProvince = getArguments().getString("countryOrProvince", "");
            isCountry = getArguments().getBoolean("isCountry");
        }
        CasesByDateApiController casesByDateApiController = new CasesByDateApiController(this);
        try {
            if (isCountry) {
                casesByDateApiController.getCountryData(countryOrProvince, false);
            } else {
                casesByDateApiController.getProvinceData(countryOrProvince, false);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private LinkedHashMap<String, Integer> sortsCasesByMonth(List<Pair<String, Integer>> casesPerDayList) {
        LinkedHashMap<String, Integer> casesByMonth = new LinkedHashMap<>();
        int cases = 0;
        int mostRecentDay = 00;
        int janCases = 0;
        int febCases = 0;
        int cumulativeFebCases = 0;
        int marchCases = 0;
        int cumulativeMarchCases = 0;
        int aprCases = 0;
        int cumulativeAprCases = 0;
        int mayCases = 0;
        int cumulativeMayCases = 0;
        int juneCases = 0;
        int cumulativeJuneCases = 0;
        int julyCases = 0;
        int cumulativeJulyCases = 0;
        int augCases = 0;
        int cumulativeAugCases = 0;
        int septCases = 0;
        int cumulativeSeptCases = 0;
        int octCases = 0;
        int cumulativeOctCases = 0;
        int novCases = 0;
        int cumulativeNovCases = 0;
        int decCases = 0;
        int cumulativeDecCases = 0;

        for (Pair<String, Integer> casesPerday : casesPerDayList) {
            System.out.println(casesPerday.first.charAt(5));

            if (casesPerday.first.charAt(5) == '0') {
                // months 1-9
                switch (casesPerday.first.charAt(6)) {
                    case '1':
                        if (casesByMonth.containsKey("Jan")) {
                            cases = casesByMonth.get("Jan");
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                casesByMonth.replace("Jan", casesPerday.second);
                                janCases = casesPerday.second;
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }

                        } else {
                            casesByMonth.put("Jan", casesPerday.second);
                        }
                        System.out.println(casesByMonth);
                        break;

                    case '2':
                        if (casesByMonth.containsKey("Feb")) {
//                            cases = casesByMonth.get("Feb");
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                // get difference between jan and feb latest day's confirmed cases
                                febCases = casesPerday.second - janCases;
                                cumulativeFebCases = casesPerday.second;
                                casesByMonth.replace("Feb", febCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("Feb", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '3':
                        if (casesByMonth.containsKey("March")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                marchCases = casesPerday.second - cumulativeFebCases;
                                cumulativeMarchCases = casesPerday.second;
                                casesByMonth.replace("March", marchCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("March", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '4':
                        if (casesByMonth.containsKey("Apr")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                aprCases = casesPerday.second - cumulativeMarchCases;
                                cumulativeAprCases = casesPerday.second;
                                casesByMonth.replace("Apr", aprCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("Apr", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '5':
                        if (casesByMonth.containsKey("May")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                mayCases = casesPerday.second - cumulativeAprCases;
                                cumulativeMayCases = casesPerday.second;
                                casesByMonth.replace("May", mayCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("May", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '6':
                        if (casesByMonth.containsKey("June")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                juneCases = casesPerday.second - cumulativeMayCases;
                                cumulativeJuneCases = casesPerday.second;
                                casesByMonth.replace("June", juneCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("June", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '7':
                        if (casesByMonth.containsKey("July")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                julyCases = casesPerday.second - cumulativeJuneCases;
                                cumulativeJulyCases = casesPerday.second;
                                casesByMonth.replace("July", julyCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("July", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '8':
                        if (casesByMonth.containsKey("Aug")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                augCases = casesPerday.second - cumulativeJulyCases;
                                cumulativeAugCases = casesPerday.second;
                                casesByMonth.replace("Aug", augCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("Aug", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                    case '9':
                        if (casesByMonth.containsKey("Sept")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                septCases = casesPerday.second - cumulativeAugCases;
                                cumulativeSeptCases = casesPerday.second;
                                casesByMonth.replace("Sept", septCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("Sept", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        System.out.println(casesByMonth);
                        break;
                }
            } else {
                System.out.println(casesPerday.first.charAt(5));
                switch (casesPerday.first.charAt(6)) {
                    case '0':
                        if (casesByMonth.containsKey("Oct")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                octCases = casesPerday.second - cumulativeSeptCases;
                                casesByMonth.replace("Oct", octCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("Oct", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        break;
                    case '1':
                        if (casesByMonth.containsKey("Nov")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                novCases = casesPerday.second - cumulativeOctCases;
                                casesByMonth.replace("Nov", novCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            } else {
                                casesByMonth.put("Nov", casesPerday.second);
                                mostRecentDay = 00;
                            }
                            break;
                        }
                    case '2':
                        if (casesByMonth.containsKey("Dec")) {
                            if (mostRecentDay < Integer.parseInt(casesPerday.first.substring(8, 10))) {
                                decCases = casesPerday.second - cumulativeNovCases;
                                cumulativeDecCases = casesPerday.second;
                                casesByMonth.replace("Dec", decCases);
                                mostRecentDay = Integer.parseInt(casesPerday.first.substring(8, 10));
                            }
                        } else {
                            casesByMonth.put("Dec", casesPerday.second);
                            mostRecentDay = 00;
                        }
                        break;
                }
            }
        }
        return casesByMonth;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bar_graph, container, false);
        AnyChartView anyChartView = view.findViewById(R.id.bar_graph);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        anyChartView.setProgressBar(progressBar);
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        for (Map.Entry<String, Integer> cases : casesPerMonth.entrySet()) {

            data.add(new ValueDataEntry(cases.getKey(), cases.getValue()));
        }
        Column column = cartesian.column(data);

        anyChartView.addFont("Raleway", "file:///android_asset/ralewaysemibold.ttf");
        column.tooltip()
                .titleFormat("{%X}").fontFamily("Raleway")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(1d)
                .format("{%Value}{groupsSeparator: }").fontFamily("Raleway");
        cartesian.animation(true);
        cartesian.title("Confirmed cases by month for " + this.countryOrProvince);
        column.color("#26C6DA");
        column.labels().fontFamily("Raleway");

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }").fontFamily("Raleway");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT).fontFamily("Raleway");
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.xAxis(0).title("Month");
        cartesian.xAxis(0).title().fontFamily("Raleway");
        cartesian.xAxis(0).labels().fontFamily("Raleway");
        cartesian.title().fontFamily("Raleway");
        cartesian.yAxis(0).title("Number of cases");
        cartesian.yAxis(0).title().fontFamily("Raleway");
        anyChartView.setChart(cartesian);

        AnyChartView deathsBarGraph = view.findViewById(R.id.deaths_bar_graph);
        APIlib.getInstance().setActiveAnyChartView(deathsBarGraph);
        ProgressBar deathsProgressBar = view.findViewById(R.id.deaths_progress_bar);
        deathsBarGraph.setProgressBar(deathsProgressBar);
        Cartesian deathsCartesian = AnyChart.column();
        List<DataEntry> deathsData = new ArrayList<>();
        // add data to the data list
        for (Map.Entry<String, Integer> deaths : deathsPerMonth.entrySet()) {
            deathsData.add(new ValueDataEntry(deaths.getKey(), deaths.getValue()));
        }

        deathsBarGraph.addFont("Raleway", "file:///android_asset/ralewaysemibold.ttf");
        Column deathsColumn = deathsCartesian.column(deathsData);
        deathsColumn.tooltip()
                .titleFormat("{%X}").fontFamily("Raleway")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(1d)
                .format("{%Value}{groupsSeparator: }");
        deathsColumn.color("#FF5722");
        deathsColumn.labels().fontFamily("Raleway");
        deathsCartesian.animation(true);
        deathsCartesian.title("Deaths by month for " + this.countryOrProvince);
        deathsCartesian.title().fontFamily("Raleway");

        deathsCartesian.yScale().minimum(0d);

        deathsCartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }").fontFamily("Raleway");
        deathsCartesian.tooltip().positionMode(TooltipPositionMode.POINT).fontFamily("Raleway");
        deathsCartesian.interactivity().hoverMode(HoverMode.BY_X);
        deathsCartesian.xAxis(0).title("Month");
        deathsCartesian.xAxis(0).title().fontFamily("Raleway");
        deathsCartesian.xAxis(0).labels().fontFamily("Raleway");
        deathsCartesian.yAxis(0).title("Number of deaths");
        deathsCartesian.yAxis(0).title().fontFamily("Raleway");
        deathsBarGraph.setChart(deathsCartesian);

        if (recoveredPerMonth.size() != 0) {
            AnyChartView recoveredBarGraph = view.findViewById(R.id.recovered_bar_graph);
            recoveredBarGraph.setVisibility(View.VISIBLE);
            APIlib.getInstance().setActiveAnyChartView(recoveredBarGraph);
            ProgressBar recoveredProgressBar = view.findViewById(R.id.recovered_progress_bar);
            recoveredBarGraph.setVisibility(View.VISIBLE);
            recoveredBarGraph.setProgressBar(recoveredProgressBar);
            Cartesian recoveredCartesian = AnyChart.column();
            List<DataEntry> recoveredData = new ArrayList<>();
            for (Map.Entry<String, Integer> recovered : recoveredPerMonth.entrySet()) {

                recoveredData.add(new ValueDataEntry(recovered.getKey(), recovered.getValue()));
            }

           recoveredBarGraph.addFont("Raleway", "file:///android_asset/ralewaysemibold.ttf");
            Column recoveredColumn = recoveredCartesian.column(recoveredData);
            recoveredColumn.tooltip()
                    .titleFormat("{%X}").fontFamily("Raleway")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(1d)
                    .format("{%Value}{groupsSeparator: }");

            recoveredColumn.labels().fontFamily("Raleway");
            recoveredColumn.color("#4CAF50");
            recoveredCartesian.animation(true);
            recoveredCartesian.title("Recovered cases by month for " + this.countryOrProvince);
            recoveredCartesian.title().fontFamily("Raleway");
            recoveredCartesian.yScale().minimum(0d);
            recoveredCartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }").fontFamily("Raleway");
            recoveredCartesian.tooltip().positionMode(TooltipPositionMode.POINT).fontFamily("Raleway");
            recoveredCartesian.interactivity().hoverMode(HoverMode.BY_X);
            recoveredCartesian.xAxis(0).title("Month");
            recoveredCartesian.xAxis(0).title().fontFamily("Raleway");
            recoveredCartesian.xAxis(0).labels().fontFamily("Raleway");
            recoveredCartesian.yAxis(0).title("Number of recovered cases");
            recoveredCartesian.yAxis(0).title().fontFamily("Raleway");
            recoveredBarGraph.setChart(recoveredCartesian);
        }
        return view;
    }

    @Override
    public void sendCasesPerDay(List<Pair<String, Integer>> casesPerDayList, List<Pair<String, Integer>> deathsPerDayList, List<Pair<String, Integer>> recoveredPerDayList) {
        casesPerMonth = sortsCasesByMonth(casesPerDayList);
        deathsPerMonth = sortsCasesByMonth(deathsPerDayList);
        recoveredPerMonth = sortsCasesByMonth(recoveredPerDayList);
    }
}
