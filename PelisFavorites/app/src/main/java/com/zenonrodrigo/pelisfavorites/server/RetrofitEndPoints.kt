package com.zenonrodrigo.pelisfavorites.server

import com.zenonrodrigo.pelisfavorites.Conf
import com.google.gson.JsonArray
import com.zenonrodrigo.pelisfavorites.Movie
import retrofit2.Response
import retrofit2.http.*

interface RetrofitEndPoints {

    @GET("movies")
    suspend fun listMovies(): Response<List<Movie>>

    @POST("movies")
    suspend fun newMovie(@Body movie: Movie?)

    @PUT("movies/{id}")
    suspend fun updateMovie(@Path("id") id: Long, @Body movie: Movie?)

    @DELETE("movies/{id}")
    suspend fun deleteMovie(@Path("id") id: Long)

    @PUT("movies/{id}")
    suspend fun updatePuntuacio(@Path("id") id: Long, @Body movie: Movie): Response<Movie>


    @GET("movies?_sort=title&_order=asc")
    suspend fun listMoviesAsc(): Response<List<Movie>>

    @GET("movies?_sort=title&_order=desc")
    suspend fun listMoviesDesc(): Response<List<Movie>>

    @GET("/conf")
    suspend fun getCity(): Response<JsonArray>

    @PUT("conf/{id}")
    suspend fun updateCity(@Path("id") confId: String, @Body conf: Conf): Response<Conf>

    @GET("movies?_sort=vote_average&_order=desc")
    suspend fun listMoviesPuntuacioDesc(): Response<List<Movie>>

    @GET("movies?_sort=vote_average&_order=asc")
    suspend fun listMoviesPuntuacioAsc(): Response<List<Movie>>




}



