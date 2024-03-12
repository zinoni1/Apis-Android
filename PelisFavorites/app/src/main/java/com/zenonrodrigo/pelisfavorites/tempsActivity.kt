package com.zenonrodrigo.pelisfavorites

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.zenonrodrigo.pelisfavorites.models.Current
import com.zenonrodrigo.pelisfavorites.server.RetrofitConnection
import com.zenonrodrigo.pelisfavorites.serverWeather.RetrofitConnectionWeather
import com.zenonrodrigo.pelisfavorites.ui.MainViewModel
import com.google.gson.JsonArray
import kotlinx.coroutines.launch

class tempsActivity : AppCompatActivity() {
    private lateinit var ubicacioTextView: TextView
    private lateinit var tempTextView: TextView
    private lateinit var condicioTextView: TextView
    private lateinit var idImatge: ImageView
    private lateinit var editTextCiutat: EditText
    private lateinit var btnCanviarCiutat: Button
    private lateinit var viewModel: MainViewModel
    private var ciutatActual: String = ""
    private var api = "a9fb0e19ccca4edb877163318240702" // Key API del weather
    private var connexioRetrofit = RetrofitConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temps)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        ubicacioTextView = findViewById(R.id.locationTextView)
        tempTextView = findViewById(R.id.temperatureTextView)
        condicioTextView = findViewById(R.id.conditionTextView)
        idImatge = findViewById(R.id.idImagen)
        editTextCiutat = findViewById(R.id.cityEditText)
        btnCanviarCiutat = findViewById(R.id.changeCityButton)

        lifecycleScope.launch {
            ciutatActual = obtenirCiutatDeRetrofit()
            editTextCiutat.setText(ciutatActual)

            obtenirDadesClima(api, ciutatActual)
        }

        btnCanviarCiutat.setOnClickListener {
            val novaCiutat = editTextCiutat.text.toString()
            if (novaCiutat.isNotEmpty()) {
                lifecycleScope.launch {
                    ciutatActual = novaCiutat
                    editTextCiutat.setText(ciutatActual)

                    viewModel.updateCity("1", novaCiutat)
                    obtenirDadesClima(api, novaCiutat)
                }
            } else {
                Toast.makeText(this, "Introduïu una ciutat", Toast.LENGTH_SHORT).show()
            }
        }
        lifecycleScope.launch {
            obtenirDadesClima(api, ciutatActual)
        }
    }

    private suspend fun obtenirCiutatDeRetrofit(): String {
        try {
            val resposta = connexioRetrofit.service.getCity()
            if (resposta.isSuccessful) {
                val ciutats: JsonArray? = resposta.body()
                if (ciutats != null && ciutats.size() > 0) {
                    val ciutat = ciutats[0].asJsonObject.get("city").asString
                    Log.d("tempsActvity", "Ciutat des del JSON: $ciutat")
                    return ciutat
                } else {
                    Log.e("temps", "La llista està buida")
                }
            } else {
                val cosError = resposta.errorBody()?.string()
                Log.e("temps", "Error $cosError")
            }
        } catch (e: Exception) {
            Log.e("temps",  "${e.message}")
        }
        return ""
    }

    private suspend fun obtenirDadesClima(apiKey: String, ciutat: String) {
        val resposta = RetrofitConnectionWeather.service.getWeather(apiKey, ciutat)
        if (resposta.isSuccessful) {
            val climaActual: Current? = resposta.body()
            if (climaActual != null) {
                actualitzarUI(climaActual)
            }
        } else {
            Log.e("temps", "Error: ${resposta.errorBody()}")
        }
    }

    private fun actualitzarUI(climaActual: Current) {
        ubicacioTextView.text = "Ubicació: ${climaActual.location.name}"
        tempTextView.text = "Temperatura Cº: ${climaActual.current.tempC}°C"

        condicioTextView.text = "Condició: ${climaActual.current.cloud}%"

        val baseIconUrl = climaActual.current.condition?.icon
        val iconUrl =
            "https:$baseIconUrl"

        Glide.with(this)
            .load(iconUrl)
            .into(idImatge)
    }
}
