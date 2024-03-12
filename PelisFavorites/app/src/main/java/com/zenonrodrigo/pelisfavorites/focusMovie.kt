package com.zenonrodrigo.pelisfavorites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.zenonrodrigo.pelisfavorites.databinding.FocusMovieBinding

class focusMovie : AppCompatActivity() {
    private lateinit var binding: FocusMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FocusMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieTitle = intent.getStringExtra("MOVIE_TITLE")
        val movieData = intent.getStringExtra("MOVIE_DATA")
        var movieScore = intent.getDoubleExtra("MOVIE_SCORE", 0.0)
        val movieImageUrl = intent.getStringExtra("MOVIE_IMAGE_URL")
        val movieAdult = intent.getBooleanExtra("MOVIE_ADULT", false)
        val moviePopularity = intent.getDoubleExtra("MOVIE_POPULARITY", 0.0)
        val movieOriginalLanguage = intent.getStringExtra("MOVIE_ORIGINAL_LANGUAGE")
        val movieOverview = intent.getStringExtra("MOVIE_OVERVIEW")

        binding.title.text = movieTitle
        binding.idAny.text = "Data: $movieData"
        binding.idAdult.text = "Adult: $movieAdult"
        binding.idPopularity.text = "Popularitat: $moviePopularity"
        binding.idOriginalLenguaje.text = "Idioma original: $movieOriginalLanguage"
        binding.idoverview.text = "Descripció: $movieOverview"

        binding.idPuntuacio1.setText(movieScore.toString())

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500${movieImageUrl}")
            .into(binding.thumb)

        binding.idEnviar.setOnClickListener {

            val newScore = binding.idPuntuacio1.text.toString().toDoubleOrNull()

            if (newScore != null && newScore in 0.0..10.0) {
                movieScore = newScore
                binding.idPuntuacio1.setText(movieScore.toString())

                val resultIntent = Intent()
                resultIntent.putExtra("UPDATED_SCORE", movieScore)

                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "La puntuació ha d'estar entre 0 i 10.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
