package com.zenonrodrigo.pelisfavorites

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.zenonrodrigo.pelisfavorites.databinding.ActivityMainBinding
import com.zenonrodrigo.pelisfavorites.ui.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var movieAdapter: movieAdapter
    private lateinit var selecPeli: Movie
    private lateinit var binding: ActivityMainBinding
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedScore = result.data?.getDoubleExtra("UPDATED_SCORE", 0.0)
                updatedScore?.let { viewModel.updateMovieScore(selecPeli, it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.rvMovie
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        movieAdapter = movieAdapter(viewModel) { movie ->
            viewModel.onMovieDelete(movie)
        }

        movieAdapter.setOnItemClickListener(object : movieAdapter.OnItemClickListener {
            override fun onItemClick(movie: Movie) {
                selecPeli = movie
                val intent = Intent(this@MainActivity, focusMovie::class.java)
                intent.putExtra("MOVIE_IMAGE_URL", movie.backdropPath)
                intent.putExtra("MOVIE_TITLE", movie.title)
                intent.putExtra("MOVIE_DATA", movie.releaseDate)
                intent.putExtra("MOVIE_SCORE", movie.voteAverage)
                intent.putExtra("MOVIE_ADULT", movie.adult)
                intent.putExtra("MOVIE_POPULARITY", movie.popularity)
                intent.putExtra("MOVIE_ORIGINAL_LANGUAGE", movie.originalLanguage)
                intent.putExtra("MOVIE_OVERVIEW", movie.overview)

                startForResult.launch(intent)
            }
        })

        recyclerView.adapter = movieAdapter

        viewModel.movies.observe(this, { movies ->
            movieAdapter.submitList(movies)
        })

        viewModel.updatedScore.observe(this, Observer {
        })
        loadFav()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ordenarASC -> {
                ordASC()
                return true
            }

            R.id.ordenarDESC -> {
                ordDESC()
                return true
            }

            R.id.idWeather -> {
                val intent = Intent(this, tempsActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.idMovieDb -> {
                novaMovie()
                return true
            }

            R.id.PuntuacioASC -> {
                ordPuntASC()
                return true
            }

            R.id.PuntuacioDESC -> {
                ordPuntDESC()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun ordASC() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.listMoviesAsc()
        }
    }

    private fun ordDESC() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.listMoviesDesc()
        }
    }

    private fun novaMovie() {
        startActivity(Intent(this, movieDbActivity::class.java))
    }

    private fun loadFav() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.loadMovies()
        }
    }

    private fun ordPuntASC() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.listMoviesPuntuacioAsc()
        }
    }

    private fun ordPuntDESC() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.listMoviesPuntuacioDesc()
        }
    }
}
