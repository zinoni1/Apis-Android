package com.zenonrodrigo.pelisfavorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zenonrodrigo.pelisfavorites.ui.MainViewModel
import com.zenonrodrigo.pelisfavorites.ui.PopularMoviesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class movieDbActivity : AppCompatActivity() {

    private lateinit var viewModel: PopularMoviesViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var moviesDBAdapter: movieDbAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.buscar_peli)

        viewModel = ViewModelProvider(this, PopularMoviesViewModel.PopularMoviesViewModelFactory()).get(PopularMoviesViewModel::class.java)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        moviesDBAdapter = movieDbAdapter()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.apply {
            recyclerView.layoutManager = GridLayoutManager(this@movieDbActivity, 2)
            adapter = moviesDBAdapter
        }
        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            Buscar()
        }

        moviesDBAdapter.setOnItemClickListener { movie ->
            submitSelectedMovie(movie)
            showScoreDialog(movie,this)
        }

        viewModel.movies.observe(this, { movies ->
            moviesDBAdapter.submitList(movies)
        })
        loadMovies()
    }

    private fun submitSelectedMovie(movie: Movie) {
        viewModel.submitSelectedMovie(this, movie)
    }
    private fun Buscar() {
        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val query = searchEditText.text.toString()
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.searchMovies(query)
        }
    }
    private fun loadMovies() {
        GlobalScope.launch(Dispatchers.IO) {
            viewModel.loadMovies()
        }
    }
    private fun showScoreDialog(movie: Movie, context: Context) {
        val scoreEditText = EditText(this)
        MaterialAlertDialogBuilder(this)
            .setTitle("Posali una puntuació")
            .setView(scoreEditText)
            .setPositiveButton("Ok") { _, _ ->
                val scoreText = scoreEditText.text.toString()
                val score = scoreText.toDoubleOrNull()

                if (score != null && score in 0.0..10.0) {
                    mainViewModel.updateMovieScore(movie, score)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "La puntuació ha de ser de 0 a 10", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

