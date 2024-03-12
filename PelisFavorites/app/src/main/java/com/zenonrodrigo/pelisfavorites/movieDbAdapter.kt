package com.zenonrodrigo.pelisfavorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zenonrodrigo.pelisfavorites.databinding.CardMovieBinding
import com.bumptech.glide.Glide

class movieDbAdapter : ListAdapter<Movie, movieDbAdapter.MovieViewHolder>(MovieDiffCallback()) {
    private var onItemClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = CardMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(movie)
        }
    }

    inner class MovieViewHolder(private val binding: CardMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.apply {
                title.text = movie.title
                idAny.text = movie.releaseDate
                idPuntuacio.text = movie.popularity.toString()

                // Cargar la imagen del póster utilizando Glide
                Glide.with(root.context)
                    .load("https://image.tmdb.org/t/p/w500${movie.backdropPath}")
                    .into(thumb)
            }
        }
    }

    private class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnItemClickListener(listener: ((Movie) -> Unit)?) {
        onItemClickListener = listener
    }
}
