package com.zenonrodrigo.pelisfavorites.ui

import android.util.Log
import androidx.lifecycle.*
import com.zenonrodrigo.pelisfavorites.Conf
import com.zenonrodrigo.pelisfavorites.Movie
import com.zenonrodrigo.pelisfavorites.server.RetrofitConnection
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _titleClicked = MutableLiveData<Movie>()
    val titleClicked: LiveData<Movie> get() = _titleClicked
    private val _loading = MutableLiveData(false)
    public val loading: LiveData<Boolean> get() = _loading

    private val _moviesCount = MutableLiveData(0)
    public val moviesCount: LiveData<Int> get() = _moviesCount

    private val _favoritesCount = MutableLiveData(0)
    public val favoritesCount: LiveData<Int> get() = _favoritesCount

    private val _movies = MutableLiveData<List<Movie>>(emptyList())
    public val movies: LiveData<List<Movie>> get() = _movies

    private val _errorApiRest = MutableLiveData<String?>(null)
    public val errorApiRest: LiveData<String?> get() = _errorApiRest

    private val _loadMoviesFav = MutableLiveData(false)
    public val loadMoviesFav: LiveData<Boolean> get() = _loadMoviesFav

    private val _updatedScore = MutableLiveData<Double>()
    val updatedScore: LiveData<Double> get() = _updatedScore

    private val _city = MutableLiveData<String>()
    val city: LiveData<String> get() = _city

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> get() = _searchResults

    fun updateMovieScore(movie: Movie, newScore: Double) {
        viewModelScope.launch {
            movie.voteAverage = newScore

            RetrofitConnection.service.updatePuntuacio(movie.id, movie)

            _updatedScore.value = newScore

        }
        loadMovies()
    }

    val MY_API_KEY = "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"
    val API_KEY = "Bearer $MY_API_KEY"

    init {
        loadMovies()
    }

    public fun loadMovies() {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            var response = RetrofitConnection.service.listMovies()

            if (response.isSuccessful) {
                _movies.value = response.body()
                _moviesCount.value = _movies.value!!.size
            }
            else {
                _errorApiRest.value = response.errorBody().toString()
            }

            _loading.value = false
        }
    }

    suspend fun updateCity(confId: String, newCity: String) {
        try {
            val conf = Conf(newCity)

            val response = RetrofitConnection.service.updateCity(confId, conf)

            if (response.isSuccessful) {
                Log.d("tempsActvity", "City updated successfully: $newCity")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("tempsActvity", "Error updating city: ${response.code()}, Body: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("tempsActvity", "Exception: ${e.message}")
        }
    }

    fun setFavoriteMovies(movies: List<Movie>) {
        _movies.value = movies.filter { it.favorite }
    }

    private fun updateFavoritosCount() {
        val favoritesCount = _movies.value?.count { it.favorite } ?: 0
        _favoritesCount.value = favoritesCount
    }

    fun onMovieDelete(movie: Movie) {
        viewModelScope.launch {
            RetrofitConnection.service.deleteMovie(movie.id!!)
            loadMovies()
        }
    }

    fun listMoviesAsc() {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            try {
                val response = RetrofitConnection.service.listMoviesAsc()
                if (response.isSuccessful) {
                    _movies.value = response.body()
                    _moviesCount.value = _movies.value?.size ?: 0
                } else {
                    _errorApiRest.value = response.errorBody()?.toString()
                }
            } catch (e: Exception) {
                _errorApiRest.value = "Error: ${e.message}"
            }

            _loading.value = false
        }
    }

    fun listMoviesDesc() {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            try {
                val response = RetrofitConnection.service.listMoviesDesc()

                if (response.isSuccessful) {
                    _movies.value = response.body()
                    _moviesCount.value = _movies.value?.size ?: 0
                } else {
                    _errorApiRest.value = response.errorBody()?.toString()
                }
            } catch (e: Exception) {
                _errorApiRest.value = "Error: ${e.message}"
            }

            _loading.value = false
        }
    }
    fun listMoviesPuntuacioDesc() {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            try {
                val response = RetrofitConnection.service.listMoviesPuntuacioDesc()

                if (response.isSuccessful) {
                    _movies.value = response.body()
                    _moviesCount.value = _movies.value?.size ?: 0
                } else {
                    _errorApiRest.value = response.errorBody()?.toString()
                }
            } catch (e: Exception) {
                _errorApiRest.value = "Error: ${e.message}"

                _loading.value = false
            }
        }
    }
    fun listMoviesPuntuacioAsc() {
        viewModelScope.launch {
            _loading.value = true
            _errorApiRest.value = null

            try {
                val response = RetrofitConnection.service.listMoviesPuntuacioAsc()

                if (response.isSuccessful) {
                    _movies.value = response.body()
                    _moviesCount.value = _movies.value?.size ?: 0
                } else {
                    _errorApiRest.value = response.errorBody()?.toString()
                }
            } catch (e: Exception) {
                _errorApiRest.value = "Error: ${e.message}"
            }
            _loading.value = false
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}