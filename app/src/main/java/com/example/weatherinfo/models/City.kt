package com.example.weatherinfo.models

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class City(
    @Expose
    val name: String?,
    @Expose
    @SerializedName("local_names")
    val localNames: LocalNames?,
    @Expose
    val lat: Double?,
    @Expose
    val lon: Double?,
    @Expose
    @SerializedName("country")
    val country: String?,
    @Expose
    val state: String?
) : Parcelable

@Keep
@Parcelize
data class LocalNames(
    @Expose
    val en: String?,
) : Parcelable