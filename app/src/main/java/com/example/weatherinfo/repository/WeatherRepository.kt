package com.example.weatherinfo.repository

import com.example.weatherinfo.api.Resource
import com.example.weatherinfo.models.City
import com.example.weatherinfo.models.WeatherItem

/**
 * Consists of list of Api calls for the weatherinfo.
 */

interface WeatherRepository {
    suspend fun getWeatherReport(word: String): Resource<WeatherItem?>
    suspend fun getValidCity(word: String): Resource<List<City>?>
}