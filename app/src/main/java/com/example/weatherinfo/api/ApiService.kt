package com.example.weatherinfo.api

import com.example.weatherinfo.models.City
import com.example.weatherinfo.models.WeatherItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to maintain the methods for the api calls.
 */
interface ApiService {
    /**
     * To get the meaning using the input string name.
     * @param q for city searching
     * @param appid for api key
     * @param units for convertion of units
     */

    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
    ): Response<WeatherItem>

    @GET("/geo/1.0/direct")
    suspend fun getValidCity(
        @Query("q") city: String,
        @Query("appid") appid: String,
    ): Response<List<City>>
}