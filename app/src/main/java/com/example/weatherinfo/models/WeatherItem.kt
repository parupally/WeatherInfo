package com.example.weatherinfo.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class to maintain list of weatherinfos.
 */

@Keep
@Parcelize
data class WeatherItem(
    @Expose
    val coord: Coord?,
    @Expose
    val weather: ArrayList<Weather> = arrayListOf(),
    @Expose
    val base: String?,
    @Expose
    val main: Main?,
    @Expose
    val visibility: Int?,
    @Expose
    val wind: Wind?,
    @Expose
    val clouds: Clouds?,
    @Expose
    val dt: Int?,
    @Expose
    val sys: Sys?,
    @Expose
    val timezone: Int?,
    @Expose
    val id: Int?,
    @Expose
    val name: String?,
    @Expose
    val cod: Int?
) : Parcelable

@Keep
@Parcelize
data class Coord(
    @Expose
    val lon: Double?,
    @Expose
    val lat: Double?
) : Parcelable

@Keep
@Parcelize
data class Weather(
    @Expose
    val id: Int?,
    @Expose
    val main: String?,
    @Expose
    val description: String?,
    @Expose
    val icon: String?
) : Parcelable

@Keep
@Parcelize
data class Main(
    @Expose
    val temp: Double?,
    @Expose
    @SerializedName("feels_like")
    val feelsLike: Double?,
    @Expose
    @SerializedName("temp_min")
    val tempMin: Double?,
    @Expose
    @SerializedName("temp_max")
    val tempMax: Double?,
    @Expose
    val pressure: Int?,
    @Expose
    val humidity: Int?
) : Parcelable

@Keep
@Parcelize
data class Clouds(
    @Expose
    val all: Int?
) : Parcelable

@Keep
@Parcelize
data class Wind(
    @Expose
    val speed: Double?,
    @Expose
    val deg: Int?
) : Parcelable

@Keep
@Parcelize
data class Sys(
    @Expose
    val type: Int?,
    @Expose
    val id: Int?,
    @Expose
    val country: String?,
    @Expose
    val sunrise: Int?,
    @Expose
    val sunset: Int?
) : Parcelable
