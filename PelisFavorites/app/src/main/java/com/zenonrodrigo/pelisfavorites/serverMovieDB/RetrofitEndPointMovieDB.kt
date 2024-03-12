package com.zenonrodrigo.pelisfavorites.serverMovieDB

import com.zenonrodrigo.pelisfavorites.models.Result
import com.zenonrodrigo.pelisfavorites.models.ResultResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitEndPointMovieDB {
    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<ResultResponse>

    @GET("search/movie")
    suspend fun searchMovies(@Query("query") query: String): Response<ResultResponse>
}