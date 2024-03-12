package com.zenonrodrigo.pelisfavorites.serverWeather

import com.zenonrodrigo.pelisfavorites.models.Current
import com.zenonrodrigo.pelisfavorites.models.Result
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import  com.zenonrodrigo.pelisfavorites.models.Weather

interface RetrofitEndPointWeather {

    @GET("current.json")
    suspend fun getWeather(
        @Query("key") key: String,
        @Query("q") city: String

    ): Response<Current>
}
