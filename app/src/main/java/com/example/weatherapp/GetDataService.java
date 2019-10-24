package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("{id}")
    Call<WeatherData> getWeatherDataCall(@Path("id") int id);
}
