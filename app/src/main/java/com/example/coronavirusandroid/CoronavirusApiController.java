package com.example.coronavirusandroid;

import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoronavirusApiController {
    CoronavirusApiControllerToUI coronavirusApiControllerToUI;
    public CoronavirusApiController(CoronavirusApiControllerToUI coronavirusApiControllerToUI) {
        this.coronavirusApiControllerToUI = coronavirusApiControllerToUI;
    }
    public interface CoronavirusApiControllerToUI {
        void updateConfirmed(int confirmed);
        void updateRecovered(int recovered);
        void updateDeaths(int deaths);
    }
    static final String BASE_URL = "https://coronavirus-tracker-api.herokuapp.com";

    public void start(String country, String provinceOrState) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CoronavirusApi coronavirusApi = retrofit.create(CoronavirusApi.class);

        Call<Countries> call = coronavirusApi.getCountries(country, provinceOrState
                );
        call.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(Call<Countries> call, Response<Countries> response) {
                int confirmed = response.body().getLatest().getConfirmed();
                int recovered = response.body().getLatest().getRecovered();
                int deaths = response.body().getLatest().getDeaths();
                coronavirusApiControllerToUI.updateConfirmed(confirmed);
                coronavirusApiControllerToUI.updateRecovered(recovered);
                coronavirusApiControllerToUI.updateDeaths(deaths);
            }

            @Override
            public void onFailure(Call<Countries> call, Throwable t) {

            }
        });
}
}
