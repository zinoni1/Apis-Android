package com.zenonrodrigo.pelisfavorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zenonrodrigo.pelisfavorites.databinding.CardMovieBinding
import com.zenonrodrigo.pelisfavorites.ui.MainViewModel

class movieAdapter(
    private val viewModel: MainViewModel,
    private val eliminarClick: (Movie) -> Unit
) : ListAdapter<Movie, movieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = CardMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, viewModel, eliminarClick, onItemClickListener)
    }

    inner class MovieViewHolder(private val binding: CardMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            movie: Movie,
            viewModel: MainViewModel,
            eliminarClick: (Movie) -> Unit,
            onItemClickListener: OnItemClickListener?
        ) {
            binding.apply {
                Glide.with(root)
                    .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                    .into(thumb)
                title.text = movie.title
                idAny.text = movie.releaseDate
                idPuntuacio.text = movie.voteAverage.toString()

                root.setOnClickListener {
                    onItemClickListener?.onItemClick(movie)
                }
                delete2.setOnClickListener {
                    eliminarClick.invoke(movie)
                }
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
}
