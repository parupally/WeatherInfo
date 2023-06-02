package com.example.weatherinfo.repository

import com.example.weatherinfo.BuildConfig
import com.example.weatherinfo.api.ApiService
import com.example.weatherinfo.api.Resource
import com.example.weatherinfo.api.RetrofitProvider
import com.example.weatherinfo.models.City
import com.example.weatherinfo.models.WeatherItem

/**
 *
 * Implementation class for [WeatherRepository]
 */

class WeatherRepositoryImpl constructor(
    private val apiService: ApiService = RetrofitProvider.client
) : WeatherRepository {
    /**
     * Get list of weatherinfo
     */
    override suspend fun getWeatherReport(word: String): Resource<WeatherItem?> {
        val response = apiService.getWeatherData(word, BuildConfig.API_KEY, "imperial")
        return if (response.isSuccessful) {
            Resource.success(response.body())
        } else {
            Resource.error(response.message())
        }
    }

    /**
     * Get list of city local names
     */
    override suspend fun getValidCity(word: String): Resource<List<City>?> {
        val response = apiService.getValidCity(word, BuildConfig.API_KEY)
        return if (response.isSuccessful) {
            Resource.success(response.body())
        } else {
            Resource.error(response.message())
        }
    }
}

