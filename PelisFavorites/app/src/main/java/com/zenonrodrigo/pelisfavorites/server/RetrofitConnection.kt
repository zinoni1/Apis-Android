package com.zenonrodrigo.pelisfavorites.server

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


object RetrofitConnection {

    private val okHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(this).build()
    }

    private val builder = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/")//10.0.2.2 es la ip per atacar a la màquina
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitEndPoints = builder.create()
}