package com.example.coronavirusandroid;

import androidx.core.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CasesByDateApiController {
    SendCasesToBarGraphFragment sendCasesToBarGraphFragment;
    SendCasesToMainActivity sendCasesToMainActivity;
    SendCasesToNotificationActivity sendCasesToNotificationActivity;
    SendCasesToNotificationBroadcast sendCasesToNotificationBroadcast;
    List<Pair<String, Integer>> casesPerDayList = new ArrayList<>();
    List<Pair<String, Integer>> recoveredPerDayList = new ArrayList<>();
    List<Pair<String, Integer>> deathsPerDayList = new ArrayList<>();
    String callerClass = "";
    String BARGRAPHFRAGMENT = "BARGRAPHFRAGMENT";
    String MAINACTIVITY = "MAINACTIVITY";
    String NOTIFICATIONACTIVITY = "NOTIIFICATIONACTIVITY";
    String NOTIFICATIONBROADCAST = "NOTIFICATIONBROADCAST";
    CountDownLatch countDownLatch = new CountDownLatch(1);
    String province = "";
    int confirmed, deaths, recovered;
    int confirmedCumulative;
    int recoveredCumulative;
    int deathsCumulative;
    int activeConfirmed;
    boolean isResponseSuccessful = true;


    public interface SendCasesToMainActivity {
        void sendCasesPerDay(List<Pair<String, Integer>> casesPerDayList,
                             List<Pair<String, Integer>> deathsPerDayList,
                             List<Pair<String, Integer>> recoveredPerDayList, int confirmedCumulative, int deathsCumulative, int recoveredCumulative);
        void displayUnavailableData();
    }

    public interface SendCasesToNotificationActivity {
        void sendCumulativeCases(int confirmed, int deaths, int recovered);
        void displayUnavailableData();
    }

    public interface SendCasesToNotificationBroadcast {
        void sendCumulativeCases(int confirmed, int deaths, int recovered);
    }


    public interface SendCasesToBarGraphFragment {
        void sendCasesPerDay(List<Pair<String, Integer>> casesPerDayList,
                                       List<Pair<String, Integer>> deathsPerDayList,
                                       List<Pair<String, Integer>> recoveredPerDayList);
    }

    public CasesByDateApiController(SendCasesToBarGraphFragment sendCasesToBarGraphFragment) {
        this.sendCasesToBarGraphFragment = sendCasesToBarGraphFragment;
        this.callerClass = BARGRAPHFRAGMENT;
    }
    public CasesByDateApiController(SendCasesToNotificationBroadcast sendCasesToNotificationBroadcast) {
        this.sendCasesToNotificationBroadcast = sendCasesToNotificationBroadcast;
        this.callerClass = NOTIFICATIONBROADCAST;
    }
    public CasesByDateApiController(SendCasesToNotificationActivity sendCasesToNotificationActivity) {
        this.sendCasesToNotificationActivity = sendCasesToNotificationActivity;
        this.callerClass = NOTIFICATIONACTIVITY;
    }
    public CasesByDateApiController(SendCasesToMainActivity sendCasesToMainActivity) {
        this.sendCasesToMainActivity = sendCasesToMainActivity;
        this.callerClass = MAINACTIVITY;
    }
    public void getProvinceData(String province, boolean isUsageForTypeOne) throws IOException, InterruptedException {
        countDownLatch = new CountDownLatch(1);
        String urlNotCumulative = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?timelines=1&province=";
        String url = "";
        OkHttpClient client = new OkHttpClient();

        this.province = province;

        url = urlNotCumulative + province;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        isResponseSuccessful = false;
                        countDownLatch.countDown();
                    } else {
                        int cases;
                        JSONObject timelines;
                        JSONObject latest;
                        JSONObject confirmedObj;
                        JSONObject deathsObj;
                        JSONObject timelineConfirmed;
                        JSONObject timelineDeaths;
                        JSONObject jsonObject;
                        JSONArray locations;
                        JSONObject firstElement;
                        String details;
                        String content = responseBody.string();

                        // parse JSON by province
                        try {

                            jsonObject = new JSONObject(content);
                            locations = jsonObject.getJSONArray("locations");
                            firstElement = locations.getJSONObject(0);
                            timelines = firstElement.getJSONObject("timelines");
                            confirmedObj = timelines.getJSONObject("confirmed");
                            deathsObj = timelines.getJSONObject("deaths");
                            int numDeaths;
                            timelineConfirmed = confirmedObj.getJSONObject("timeline");
                            timelineDeaths = deathsObj.getJSONObject("timeline");

                            jsonObject = new JSONObject(content);
                            latest = jsonObject.getJSONObject("latest");
                            confirmedCumulative = latest.getInt("confirmed");
                            recoveredCumulative = latest.getInt("recovered");
                            deathsCumulative = latest.getInt("deaths");

                            for (Iterator<String> it = timelineConfirmed.keys(); it.hasNext(); ) {
                                String timelineKey = it.next();
                                cases = timelineConfirmed.getInt(timelineKey);
                                casesPerDayList.add(new Pair<>(timelineKey, cases));
                            }
                            for (Iterator<String> it = timelineDeaths.keys(); it.hasNext(); ) {
                                String timelineKey = it.next();
                                numDeaths = timelineDeaths.getInt(timelineKey);
                                deathsPerDayList.add(new Pair<>(timelineKey, numDeaths));
                            }


                            activeConfirmed = confirmedCumulative - (recoveredCumulative + deathsCumulative);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    countDownLatch.countDown();
                }
            }
        });
        try {
            countDownLatch.await();
            // if info is unavailable
            if (! isResponseSuccessful) {
                isResponseSuccessful = true;
                if (callerClass.contentEquals(MAINACTIVITY)) {
                    sendCasesToMainActivity.displayUnavailableData();
                    return;
                }
                else if (callerClass.contentEquals(NOTIFICATIONACTIVITY)) {
                    sendCasesToNotificationActivity.displayUnavailableData();
                    return;
                }
            } else {
                    if (callerClass.contentEquals(MAINACTIVITY)) {
                        sendCasesToMainActivity.sendCasesPerDay(casesPerDayList, deathsPerDayList, recoveredPerDayList,
                                confirmedCumulative, deathsCumulative, recoveredCumulative);
                    }
                    else if (callerClass.contentEquals(NOTIFICATIONACTIVITY)) {
                        sendCasesToNotificationActivity.sendCumulativeCases(confirmedCumulative,
                                deathsCumulative, recoveredCumulative);
                    }
                    else if (callerClass.contentEquals(NOTIFICATIONBROADCAST)) {
                        sendCasesToNotificationBroadcast.sendCumulativeCases(confirmedCumulative,
                                deathsCumulative, recoveredCumulative);
                    }
                    else if (callerClass.contentEquals(BARGRAPHFRAGMENT)) {
                        sendCasesToBarGraphFragment.sendCasesPerDay(casesPerDayList, deathsPerDayList,
                                recoveredPerDayList);
                    }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void getCountryData(String country, boolean isCumulative) {
        countDownLatch = new CountDownLatch(1);
        String url = "https://api.covid19api.com/total/country/" + country;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    String content = responseBody.string();
                    JSONArray countryInfo = new JSONArray(content);
                    JSONObject countryDailyInfo;
                    String date;
                        for (int i = 0; i < countryInfo.length(); i++) {
                            countryDailyInfo = countryInfo.getJSONObject(i);
                            date = countryDailyInfo.getString("Date");
                            confirmed = countryDailyInfo.getInt("Confirmed");
                            casesPerDayList.add(new Pair<>(date, confirmed));
                            deaths = countryDailyInfo.getInt("Deaths");
                            deathsPerDayList.add(new Pair<>(date, deaths));
                            recovered = countryDailyInfo.getInt("Recovered");
                            recoveredPerDayList.add(new Pair<>(date, recovered));
                        }
                        // get the last item in array - the most recent/current results
                        countryDailyInfo = countryInfo.getJSONObject(countryInfo.length() - 1);
                        confirmedCumulative = countryDailyInfo.getInt("Confirmed");
                        deathsCumulative = countryDailyInfo.getInt("Deaths");
                        recoveredCumulative = countryDailyInfo.getInt("Recovered");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();

            // if info is unavailable
            if (! isResponseSuccessful) {
                isResponseSuccessful = true;
                if (callerClass.contentEquals(MAINACTIVITY)) {
                    sendCasesToMainActivity.displayUnavailableData();
                    return;
                }
                else if (callerClass.contentEquals(NOTIFICATIONACTIVITY)) {
                    sendCasesToNotificationActivity.displayUnavailableData();
                    return;
                }
            } else {

                if (callerClass.contentEquals(MAINACTIVITY)) {
                    sendCasesToMainActivity.sendCasesPerDay(casesPerDayList, deathsPerDayList, recoveredPerDayList,
                            confirmedCumulative, deathsCumulative, recoveredCumulative);
                }
                else if (callerClass.contentEquals(NOTIFICATIONACTIVITY)) {
                    sendCasesToNotificationActivity.sendCumulativeCases(confirmed, deaths, recovered);
                }
                else if (callerClass.contentEquals(NOTIFICATIONBROADCAST)) {
                    sendCasesToNotificationBroadcast.sendCumulativeCases(confirmed, deaths, recovered);
                }
                else if (callerClass.contentEquals(BARGRAPHFRAGMENT)) {
                    sendCasesToBarGraphFragment.sendCasesPerDay(casesPerDayList, deathsPerDayList,
                            recoveredPerDayList);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
