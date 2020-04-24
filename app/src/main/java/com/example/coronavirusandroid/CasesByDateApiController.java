package com.example.coronavirusandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.core.util.Pair;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class CasesByDateApiController {
    SendCasesToFragment sendCasesToFragment;
    List<Pair<String, Integer>> casesPerDayList = new ArrayList<>();
    List<Pair<String, Integer>> recoveredPerDayList = new ArrayList<>();
    List<Pair<String, Integer>> deathsPerDayList = new ArrayList<>();
    CountDownLatch countDownLatch = new CountDownLatch(1);
    String province = "";
    String country = "";
    Context context;



    public interface SendCasesToFragment {
        void sendCasesPerDayToFragment(List<Pair<String, Integer>> casesPerDayList, List<Pair<String, Integer>> deathsPerDayList, List<Pair<String, Integer>> recoveredPerDayList, String provinceOrCountry);
    }

    public CasesByDateApiController(SendCasesToFragment sendCasesToFragment, Context context) {
        this.sendCasesToFragment = sendCasesToFragment;
        this.context = context;
    }
    public void run(String url, String province, String country) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();
//        List<Pair<String, Integer>> casesPerDayList = new ArrayList<>();
        HashMap<String, Integer> casesPerMonth = new HashMap<>();
        this.province = province;
        this.country = country;
        if (!province.contentEquals("")) {
            url = url + province;

        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                countDownLatch.countDown();
            }

            //
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
////
////                    int cases;
////                    // go through response body and parse for timelines and get cases for each day
////                    JSONObject jsonObject = new JSONObject(response.body().string());
////                    JSONArray locations = jsonObject.getJSONArray("locations");
////                    JSONObject firstElement = locations.getJSONObject(0);
////                    JSONObject timelines = firstElement.getJSONObject("timelines");
////                    JSONObject confirmed = timelines.getJSONObject("confirmed");
////                    JSONObject timeline = confirmed.getJSONObject("timeline");
////                    for (Iterator<String> it = timeline.keys(); it.hasNext(); ) {
////                        String timelineKey = it.next();
////                        cases = timeline.getInt(timelineKey);
////                        casesPerDayList.add(new Pair<>(timelineKey, cases));
////                    }
////                    System.out.println(casesPerDayList);
//
////                        Runnable runnable = new Runnable() {
////                            @Override
////                            public void run() {
////                                try {
////                                    casesPerDayList = parseJSON(responseBody, country, province);
////                                } catch (IOException e) {
////                                    e.printStackTrace();
////                                } catch (InterruptedException e) {
////                                    e.printStackTrace();
////                                }
////                            }
////                        };
////
////                        Thread backgroundThread = new Thread(runnable);
////                        backgroundThread.start();
                    int cases;
                    JSONObject timelines;
                    JSONObject confirmed;
                    JSONObject deaths;
                    JSONObject timelineConfirmed;
                    JSONObject timelineDeaths;
                    JSONObject jsonObject;
                    JSONArray locations;
                    JSONObject firstElement;
                    String content = responseBody.string();
//                    BufferedSource source = responseBody.source();
//                    source.request(Long.MAX_VALUE); // request the entire body.
//                    Buffer buffer = source.buffer();
//// clone buffer before reading from it
//                    String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
//                    List<Pair<String, Integer>> casesByDayList = new ArrayList<>();
//                    if (province.contentEquals("")) {
                        // parse JSON by the country


//                        try {
                        // go through response body and parse for timelines and get cases for each day
//                            jsonObject = new JSONObject(responseBodyString);
//                            locations = jsonObject.getJSONArray("locations");
//
//                            // find the corresponding country given by user
//                            int i = 0;
//                            Boolean foundCountry = false;
//                            while (i < locations.length() && !foundCountry) {
//                                if (locations.getJSONObject(i).get("country") == country) {
//                                    // get the confirmed cases in the timeline
//                                    timelines = locations.getJSONObject(i).getJSONObject("timelines");
//                                    confirmed = timelines.getJSONObject("confirmed");
//                                    timeline = confirmed.getJSONObject("timeline");
//
//                                    for (Iterator<String> it = timeline.keys(); it.hasNext(); ) {
//                                        String timelineKey = it.next();
//                                        cases = timeline.getInt(timelineKey);
//                                        casesPerDayList.add(new Pair<>(timelineKey, cases));
//                                    }
//                                }
//                                i++;
//                            }

//                        }
//                        catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else {
                        // parse JSON by province
                        try {
                            jsonObject = new JSONObject(content);
                            locations = jsonObject.getJSONArray("locations");
                            firstElement = locations.getJSONObject(0);
                            timelines = firstElement.getJSONObject("timelines");
                            confirmed = timelines.getJSONObject("confirmed");
                            deaths = timelines.getJSONObject("deaths");
                            int numDeaths;
                            timelineConfirmed = confirmed.getJSONObject("timeline");
                            timelineDeaths = deaths.getJSONObject("timeline");

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                    ParseJSON parseJSON = new ParseJSON();
//                    parseJSON.execute(content, country, province, url);
//                    System.out.println("1");
//
//
                        System.out.println(casesPerDayList);
//                }
                    }
                countDownLatch.countDown();
                }

//        System.out.println("2");
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (province.contentEquals("")) {
            sendCasesToFragment.sendCasesPerDayToFragment(casesPerDayList, deathsPerDayList, recoveredPerDayList, country);
        } else {
            sendCasesToFragment.sendCasesPerDayToFragment(casesPerDayList, deathsPerDayList, recoveredPerDayList, province);
        }
    }
    public void getCountryData(String country) {
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
                    int confirmed;
                    int recovered;
                    int deaths;
                    // loop through the JSONArray to get all cases for each day
                    for (int i = 0; i < countryInfo.length(); i++) {
                        countryDailyInfo = countryInfo.getJSONObject(i);
                        date = countryDailyInfo.getString("Date");
                        confirmed = countryDailyInfo.getInt("Confirmed");
                        casesPerDayList.add(new Pair<>(date, confirmed));
                    }
                    for (int i = 0; i < countryInfo.length(); i++) {
                        countryDailyInfo = countryInfo.getJSONObject(i);
                        date = countryDailyInfo.getString("Date");
                        deaths = countryDailyInfo.getInt("Deaths");
                        deathsPerDayList.add(new Pair<>(date, deaths));
                    }
                    for (int i = 0; i < countryInfo.length(); i++) {
                        countryDailyInfo = countryInfo.getJSONObject(i);
                        date = countryDailyInfo.getString("Date");
                        recovered = countryDailyInfo.getInt("Recovered");
                        recoveredPerDayList.add(new Pair<>(date, recovered));
                    }
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
            sendCasesToFragment.sendCasesPerDayToFragment(casesPerDayList, deathsPerDayList, recoveredPerDayList, country);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

//    private List<Pair<String, Integer>> parseJSON(ResponseBody responseBody, String country, String province) throws IOException, InterruptedException {
//        int cases;
//        JSONObject timelines;
//        JSONObject confirmed;
//        JSONObject timeline;
//        JSONObject jsonObject;
//        JSONArray locations;
//        JSONObject firstElement;
//        String content = responseBody.string();
//        List<Pair<String, Integer>> casesByDayList = new ArrayList<>();
//        if (province.contentEquals("")) {
//            // parse JSON by the country
//
//
//            try {
//                // go through response body and parse for timelines and get cases for each day
//                jsonObject = new JSONObject(content);
//                System.out.println(jsonObject);
//                locations = jsonObject.getJSONArray("locations");
//
//                // find the corresponding country given by user
//                for (int i = 0; i < locations.length(); i++) {
//                    if (locations.getJSONObject(i).get("country") == country) {
//                        // get the confirmed cases in the timeline
//                        timelines = locations.getJSONObject(i).getJSONObject("timelines");
//                        confirmed = timelines.getJSONObject("confirmed");
//                        timeline = confirmed.getJSONObject("timeline");
//
//                        for (Iterator<String> it = timeline.keys(); it.hasNext(); ) {
//                            String timelineKey = it.next();
//                            cases = timeline.getInt(timelineKey);
//                            casesByDayList.add(new Pair<>(timelineKey, cases));
//                        }
//                    }
//                }
//
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else {
//            // parse JSON by province
//            try {
//                jsonObject = new JSONObject(content);
//                locations = jsonObject.getJSONArray("locations");
//                firstElement = locations.getJSONObject(0);
//                timelines = firstElement.getJSONObject("timelines");
//                confirmed = timelines.getJSONObject("confirmed");
//                timeline = confirmed.getJSONObject("timeline");
//                for (Iterator<String> it = timeline.keys(); it.hasNext(); ) {
//                    String timelineKey = it.next();
//                    cases = timeline.getInt(timelineKey);
//                    casesByDayList.add(new Pair<>(timelineKey, cases));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println(casesByDayList + "===");
//        return casesByDayList;
//    }


}
