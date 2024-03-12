package com.zenonrodrigo.pelisfavorites.models

import com.google.gson.annotations.SerializedName

data class Weather(
    val location: Location,
    val current: Current,
)

data class Current(
    val location: Location,
    val current: Current,

    @SerializedName("last_updated_epoch")
    val lastUpdatedEpoch: Long,
    @SerializedName("last_updated")
    val lastUpdated: String,
    @SerializedName("temp_c")
    val tempC: Double,
    @SerializedName("temp_f")
    val tempF: Double,
    @SerializedName("is_day")
    val isDay: Long,
    val condition: Condition,
    @SerializedName("wind_mph")
    val windMph: Double,
    @SerializedName("wind_kph")
    val windKph: Double,
    @SerializedName("wind_degree")
    val windDegree: Long,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("pressure_mb")
    val pressureMB: Double,
    @SerializedName("pressure_in")
    val pressureIn: Double,
    @SerializedName("precip_mm")
    val precipMm: Double,
    @SerializedName("precip_in")
    val precipIn: Double,
    val humidity: Long,
    val cloud: Long,
    @SerializedName("feelslike_c")
    val feelslikeC: Double,
    @SerializedName("feelslike_f")
    val feelslikeF: Double,
    @SerializedName("vis_km")
    val visKM: Double,
    @SerializedName("vis_miles")
    val visMiles: Double,
    val uv: Long,
    @SerializedName("gust_mph")
    val gustMph: Double,
    @SerializedName("gust_kph")
    val gustKph: Double
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Long
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id")
    val tzID: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,
    val localtime: String
)
