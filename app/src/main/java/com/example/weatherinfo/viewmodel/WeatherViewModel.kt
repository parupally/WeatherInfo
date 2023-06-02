package com.example.weatherinfo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherinfo.api.Status
import com.example.weatherinfo.models.WeatherItem
import com.example.weatherinfo.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * View Model to handle logic for weatherinfos
 */

class WeatherViewModel constructor(
    private val repository: WeatherRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    /**
     * Notifies if no data is received.
     */
    private val _noDataReceived = MutableLiveData<Boolean>()
    val noDataReceived: LiveData<Boolean> = _noDataReceived

    /**
     * Notifies the weatherinfo List.
     */
    private val _weatherInfoList = MutableLiveData<List<WeatherItem>?>()
    val weatherInfoList: MutableLiveData<List<WeatherItem>?> = _weatherInfoList

    /**
     * Provides the status of the API call.
     */
    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status


    /**
     * Get the last search data to save.
     */
    private val _savedCity = MutableLiveData<String>()
    val savedCity: LiveData<String> = _savedCity

    /**
     * Get the list of weatherinfos when user searches.
     * @param word from user input
     */
    fun onClickSearch(word: String) {
        _status.postValue(Status.Waiting)
        val exceptionHandler = CoroutineExceptionHandler { _, _ ->
            _status.postValue(Status.ERROR)
        }
        viewModelScope.launch(dispatcher + exceptionHandler) {
            val cityResponse = repository.getValidCity(word = word)
            if (cityResponse.status == Status.SUCCESS) {
                val cityData = cityResponse.data
                if (!cityData.isNullOrEmpty() && cityData[0].country.equals("US")) {
                    val response =
                        repository.getWeatherReport(word = cityData[0].localNames?.en.toString())
                    if (response.status == Status.SUCCESS) {
                        val details = response.data
                        if (details == null) {
                            _noDataReceived.postValue(true)
                        } else {
                            // We can convert the response model to Domain objects that can provide data to UI.
                            _weatherInfoList.postValue(listOf(details))
                            _savedCity.postValue(word)
                        }
                    }
                    _status.postValue(response.status)
                } else {
                    _noDataReceived.postValue(true)
                }
            }
        }

    }
}