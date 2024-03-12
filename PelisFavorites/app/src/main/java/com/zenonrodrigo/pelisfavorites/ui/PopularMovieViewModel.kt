package com.zenonrodrigo.pelisfavorites.ui

import android.content.Context
import androidx.lifecycle.*
import com.zenonrodrigo.pelisfavorites.Movie
import com.zenonrodrigo.pelisfavorites.server.RetrofitConnection
import com.zenonrodrigo.pelisfavorites.serverMovieDB.RetrofitConnectionMovieDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PopularMoviesViewModel: ViewModel() {
    private val _loading = MutableLiveData(false)

    public val loading: LiveData<Boolean> get() = _loading

    private val _moviesCount = MutableLiveData(0)
    public val moviesCount: LiveData<Int> get() = _moviesCount

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    public val movies: LiveData<List<Movie>> get() = _movies

    private val _errorApiRest = MutableLiveData<String?>(null)
    public val errorApiRest: LiveData<String?> get() = _errorApiRest

    init {
        loadMovies()
    }

    public fun loadMovies() {

        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            var response = RetrofitConnectionMovieDB.service.getPopularMovies()

            if (response.isSuccessful) {
                _movies.value = response.body()?.results
                _moviesCount.value = _movies.value!!.size
            } else {
                _errorApiRest.value = response.errorBody().toString()
            }

            _loading.value = false
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null
            var response = RetrofitConnectionMovieDB.service.searchMovies(query)
            if (response.isSuccessful) {
                _movies.value = response.body()?.results
                _moviesCount.value = _movies.value!!.size
            } else {
                _errorApiRest.value = response.errorBody().toString()
            }
            _loading.value = false
        }
    }

    fun submitSelectedMovie(context: Context, movie: Movie) {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            try {
                val response = RetrofitConnection.service.newMovie(movie)

                val updatedMoviesResponse = RetrofitConnection.service.listMovies()
                if (updatedMoviesResponse.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        _movies.value = updatedMoviesResponse.body()
                        _moviesCount.value = _movies.value?.size ?: 0

                    }
                } else {
                    throw RuntimeException("Error al cargar la llista després de posar una nova película.")
                }
            } catch (e: Exception) {
                _errorApiRest.value = e.message
            } finally {
                _loading.value = false
            }
        }
}

    @Suppress("UNCHECKED_CAST")
    class PopularMoviesViewModelFactory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PopularMoviesViewModel::class.java)) {
                return PopularMoviesViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}