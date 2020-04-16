package com.example.coronavirusandroid;

import java.util.Optional;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface CoronavirusApi {

    @GET("v2/locations")
    Call<Countries> getCountriesWithProvince(@Query("country") String country, @Query("province") String province);
    @GET("v2/locations")
    Call<Countries> getCountries(@Query("country") String country);
}

