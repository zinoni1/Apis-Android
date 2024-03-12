package com.zenonrodrigo.pelisfavorites.serverWeather

import com.zenonrodrigo.pelisfavorites.server.RetrofitEndPoints
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitConnectionWeather {
    private val okHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }
    private val builder = Retrofit.Builder()
        .baseUrl("http://api.weatherapi.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitEndPointWeather = builder.create()
}
