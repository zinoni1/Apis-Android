package com.zenonrodrigo.pelisfavorites.serverMovieDB

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitConnectionMovieDB {

    private val okHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization","Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIxOGYyMjhkMDMyMWUzZmQxYjhmMWIwZWI3NDQ1MTU4ZCIsInN1YiI6IjY1ZDRjMTZmZWI3OWMyMDE0YjQ0MDgyMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.tYBPTCLl-xxbxy00tMbjk4PBli9sm4qzv5KxRkP_SQk")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private val builder = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: RetrofitEndPointMovieDB = builder.create()

}

