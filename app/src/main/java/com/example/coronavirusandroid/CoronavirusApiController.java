//package com.example.coronavirusandroid;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.view.View;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.util.Optional;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class CoronavirusApiController {
//    CoronavirusApiControllerToUI coronavirusApiControllerToUI;
//    CoronavirusApiControllerError coronavirusApiControllerError;
//    AlertDialog.Builder alertBuilder;
//    AlertDialog alert;
//    public CoronavirusApiController(CoronavirusApiControllerToUI coronavirusApiControllerToUI) {
//        this.coronavirusApiControllerToUI = coronavirusApiControllerToUI;
//    }
//
//    public CoronavirusApiController(CoronavirusApiControllerError coronavirusApiControllerError) {
//        this.coronavirusApiControllerError = coronavirusApiControllerError;
//    }
//    public interface CoronavirusApiControllerToUI {
//        void updateConfirmed(int confirmed);
//        void updateRecovered(int recovered);
//        void updateDeaths(int deaths);
//        void updateNotifs(int confirmed, int recovered, int deaths);
//        void displayChart(int confirmed, int recovered, int deaths, String province, String country);
//        void displayUnavailableData();
//    }
//
//    public interface CoronavirusApiControllerError {
//        void displayUnavailableData();
////        void displayNotifsBySpecifiedTime();
//    }
//
//    static final String BASE_URL = "https://coronavirus-tracker-api.herokuapp.com";
//
//    public void start(String country, String provinceOrState, String description) {
//        Call<Countries> call;
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        CoronavirusApi coronavirusApi = retrofit.create(CoronavirusApi.class);
//
//        // check if province and country are valid; check if province is empty
//        if (provinceOrState.contentEquals("")) {
//            call = coronavirusApi.getCountries(country);
//        } else {
//            call = coronavirusApi.getCountriesWithProvince(provinceOrState
//            );
//        }
//        // enqueue the countries api to check if the country input is valid
//
//        call.enqueue(new Callback<Countries>() {
//            @Override
//            public void onResponse(Call<Countries> call, Response<Countries> response) {
//
//                // if source/api doesn't have any info regarding this province then display message
//
//                try {
//                    response.body().getLocations();
//                } catch (Exception e) {
//                    if (description.contentEquals("NORESULTSNOTIF")) {
//                        coronavirusApiControllerError.displayUnavailableData();
//                    } else {
//                        coronavirusApiControllerToUI.displayUnavailableData();
//                    }
//                    return;
//                }
//                int confirmed = response.body().getLatest().getConfirmed();
//
//                int recovered = response.body().getLatest().getRecovered();
//                int deaths = response.body().getLatest().getDeaths();
//                if (description.contentEquals(MainActivity.DISPLAY_NUMBERS)) {
//                    coronavirusApiControllerToUI.updateConfirmed(confirmed);
//                    coronavirusApiControllerToUI.updateRecovered(recovered);
//                    coronavirusApiControllerToUI.updateDeaths(deaths);
//                } else if (description.contentEquals(MainActivity.GET_NOTIFICATIONS)) {
//                    coronavirusApiControllerToUI.updateNotifs(confirmed, recovered, deaths);
//                } else if (description.contentEquals(MainActivity.pieChart)) {
//                    coronavirusApiControllerToUI.displayChart(confirmed, recovered, deaths, provinceOrState, country);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Countries> call, Throwable t) {
//
//            }
//        });
//}
//}
